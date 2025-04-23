package com.sporttime.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.sporttime.dto.DatosClimaticoResponse;
import com.sporttime.dto.OpenWeatherResponse;
import com.sporttime.dto.WeatherData;
import com.sporttime.exception.CiudadNoEncontradaException;
import com.sporttime.exception.ServiceUnavailableException;
import com.sporttime.mapper.DatosClimaticoMapper;
import com.sporttime.model.CiudadClima;
import com.sporttime.model.CondicionClimatica;
import com.sporttime.model.DatosClimaticos;
import com.sporttime.model.Deporte;
import com.sporttime.repository.CiudadRepository;
import com.sporttime.repository.DatosClimaticosRepository;
import com.sporttime.repository.DeporteRepository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClimaServiceImpl implements ClimaService {

	private final DatosClimaticosRepository climaRepository; 
	private final DeporteRepository deporteRepository;
	private final CiudadRepository ciudadRepository;
	
	private final DatosClimaticoMapper datosClimaticoMapper;
	private final RestTemplate restTemplate;
	
	private final RecomendacionDeportivaService recomendacionService;
	
	@Value("${openweathermap.api.url}")
	private String apiUrl;

	@Value("${openweathermap.api.key}")
	private String apiKey;

	@Override
	@Transactional 
    @Cacheable(value = "climaCache", key = "#nombreCiudad.toLowerCase() + '_' + T(java.time.LocalDate).now()") // Busca en caché primero  
	public List<DatosClimaticoResponse> obtenerDatosClimaticos(String nombreCiudad) {
		if (nombreCiudad == null || nombreCiudad.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ciudad no puede estar vacío");
        }
		String ciudad = nombreCiudad.toLowerCase();
        LocalDate fecha = LocalDate.now(); // NUEVO: obtenemos la fecha actual
		
		log.info("Buscando datos para: {} en fecha {}", ciudad, fecha);
		
		try {
			
	        // busqueda por ciudad y fecha BD.
			boolean existenDatosHoy = ciudadRepository.existsByNombreAndFecha(ciudad, fecha);
			List<DatosClimaticos> datos;
	        
			if (existenDatosHoy) {	
				log.info("Datos encontrados en la base de datos.");
	            datos = climaRepository.findByCiudadNombre(ciudad);
	            System.out.println("datos de la BD");   
			} else {
				log.info("Datos no encontrados en la BD. Consultando la API...");
				eliminarRegistrosAnteriores(ciudad);
                datos = obtenerYGuardarDatosDeApi(ciudad);
                System.out.println("datos de la api");
				
				if (datos.isEmpty()) {
					throw new CiudadNoEncontradaException(ciudad);
				}
			}
			
			return datos.stream()
					.map(datosClimaticoMapper::toResponse)
					.toList();
			
		} catch (HttpClientErrorException.NotFound e) {
			 // OpenWeatherMap devuelve 404 cuando la ciudad no existe
	        throw new CiudadNoEncontradaException(ciudad);
		} catch (RestClientException e) {
			// Cualquier otra excepción al conectarse con la API
	        throw new ServiceUnavailableException("No se pudo conectar con OpenWeatherMap.");
		}
	}
	
	private void eliminarRegistrosAnteriores(String ciudad) {
        List<CiudadClima> registrosAnteriores = ciudadRepository.findByNombre(ciudad);
        if (!registrosAnteriores.isEmpty()) {
            log.info("Eliminando registros anteriores para la ciudad: {}", ciudad);
            ciudadRepository.deleteAll(registrosAnteriores);
        }
    }
	
	//@Transactional // Sin readOnly para operaciones de escritura
	public List<DatosClimaticos> obtenerYGuardarDatosDeApi(String ciudad) {
        List<DatosClimaticos> datos = obtenerDatosDeApi(ciudad);
        return climaRepository.saveAll(datos);
    }

	
	//@Cacheable(value = "datosClimaticos", key = "#ciudad")
	public List<DatosClimaticos> obtenerDatosDeApi(String ciudad) {
		try {
			// Construye la URL de la API
			String url = String.format("%s?q=%s&units=metric&lang=es&appid=%s", apiUrl, ciudad, apiKey);
			
			// Llama a la API
			OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);
			
			if (response == null || response.getData().isEmpty()) {
				throw new CiudadNoEncontradaException("Datos no disponibles para: " + ciudad);
			}
			
			// Procesa los datos por día
			return response.getData().stream()
	                .collect(Collectors.groupingBy(d -> LocalDate.parse(d.getFechaTexto(), 
	                		DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
	                .entrySet().stream()
	                .sorted(Map.Entry.comparingByKey()) // Ordena por fecha (LocalDate implementa Comparable)
	                .map(entry -> {
	                	DatosClimaticos dc = mapearDatosDiarios(ciudad, entry.getKey(), entry.getValue());
	                	calcularRecomendacionDeportiva(dc);
	                    return dc;
	                })
	                .toList();
			
		} catch (HttpClientErrorException.NotFound e) {
			// Ciudad no encontrada en la API
	        throw new CiudadNoEncontradaException(ciudad);
		} catch (RestClientException e) {
			// Error de red o servicio no disponible
	        throw new ServiceUnavailableException("No se pudo obtener datos de OpenWeatherMap. Intente más tarde.");
		}
	}
	
	private void calcularRecomendacionDeportiva(DatosClimaticos datosClimatico) {
		try {
			RecomendacionDeportivaService.Recomendacion recomendacion = 
	                recomendacionService.calcularRecomendacion(
	                    datosClimatico.getAgvTemperaturaDiaria(),
	                    datosClimatico.getAgvVelocidadViento(),
	                    datosClimatico.getAgvHumedad());
			
			Optional<Deporte> deporte = deporteRepository.findByNombre(recomendacion.deporte());
			deporte.ifPresent(datosClimatico::setDeporteRecomendado);
	        datosClimatico.setPorcentajeIdoneidad(recomendacion.porcentaje());
		
		} catch (Exception e) {
			log.error("Error calculando recomendación deportiva", e);
            datosClimatico.setPorcentajeIdoneidad(0);
		}
	}
	
	private DatosClimaticos mapearDatosDiarios(String ciudad, LocalDate fecha, List<WeatherData> datosDia) {		
	    
		CiudadClima datosCiudad = new CiudadClima();
		datosCiudad.setNombre(ciudad);
		datosCiudad.setDiaSemana(fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")));
		datosCiudad.setFecha(fecha);
		datosCiudad = ciudadRepository.save(datosCiudad);

		
		
		DatosClimaticos datosClimatico = new DatosClimaticos();	    	 
		datosClimatico.setCiudad(datosCiudad);
		datosClimatico.setAgvTemperaturaDiaria(calcularPromedio(datosDia, d -> d.getMain().getTemp()));
		datosClimatico.setTemperaturaMax(datosDia.stream().mapToDouble(d -> d.getMain().getTempMax()).max().orElse(0));
		datosClimatico.setTemperaturaMin(datosDia.stream().mapToDouble(d -> d.getMain().getTempMin()).min().orElse(0));
		datosClimatico.setAgvVelocidadViento(calcularPromedio(datosDia, d -> d.getWind().getSpeed()));
		datosClimatico.setAgvHumedad(calcularPromedio(datosDia, d -> d.getMain().getHumidity().doubleValue()));
		
		if (datosClimatico.getCondicionClimatica() == null) {
		    datosClimatico.setCondicionClimatica(new CondicionClimatica());
		}
		datosClimatico.getCondicionClimatica().setEstado(calcularModa(datosDia, w -> w.getWeather().get(0).getMain()));
		datosClimatico.getCondicionClimatica().setDescripcion(calcularModa(datosDia, w -> w.getWeather().get(0).getDescription()));
		datosClimatico.getCondicionClimatica().setIcono(calcularModa(datosDia, w -> w.getWeather().get(0).getIcon()));

		
		return datosClimatico;
	}
	
	// Calcula promedio redondeado a 2 decimales
	private Double calcularPromedio(List<WeatherData> datos, Function<WeatherData, Double> extractor) {
		double promedio = datos.stream()
				.map(extractor)
				.filter(Objects::nonNull)
				.mapToDouble(d -> d)
				.average()
				.orElse(0.0);
		
		 return Math.round(promedio * 100.0) / 100.0;
	}

	// Calcula el valor más frecuente (moda
	private String calcularModa(List<WeatherData> datos, Function<WeatherData, String > extractor) {
		return datos.stream()
                .map(extractor)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
	}
}
