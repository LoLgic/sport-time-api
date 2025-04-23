package com.sporttime.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sporttime.dto.CiudadResponse;
import com.sporttime.dto.ClimaVisualResponse;
import com.sporttime.dto.DatosClimaticoResponse;
import com.sporttime.dto.DeporteResponse;
import com.sporttime.model.CiudadClima;
import com.sporttime.model.CondicionClimatica;
import com.sporttime.model.DatosClimaticos;
import com.sporttime.model.Deporte;

@Component
public class DatosClimaticoMapper {
	
	public  DatosClimaticoResponse toResponse(DatosClimaticos datosClimatico) {
        return new DatosClimaticoResponse(
        		toCiudadResponse(datosClimatico.getCiudad()),
                datosClimatico.getAgvTemperaturaDiaria(),
                datosClimatico.getTemperaturaMax(),
                datosClimatico.getTemperaturaMin(),
                datosClimatico.getAgvVelocidadViento(),
                datosClimatico.getAgvHumedad(),
                toClimaVisualResponse(datosClimatico.getCondicionClimatica()),
                toDeporteResponse(datosClimatico.getDeporteRecomendado()),
                datosClimatico.getPorcentajeIdoneidad()
        );
    }
	
	private CiudadResponse toCiudadResponse(CiudadClima ciudadClima) {
		if (ciudadClima == null) return null;
		return new CiudadResponse(
				ciudadClima.getId(), 
				ciudadClima.getNombre(), 
				ciudadClima.getDiaSemana(), 
				ciudadClima.getFecha());
	}
	
	private ClimaVisualResponse toClimaVisualResponse(CondicionClimatica clima) {
        if (clima == null) return null;
        return new ClimaVisualResponse(clima.getEstado(), clima.getDescripcion(), clima.getIcono());
    }
	
	private DeporteResponse toDeporteResponse(Deporte deporte) {
		if (deporte == null) return null;
		return new DeporteResponse(deporte.getId(), deporte.getNombre(), deporte.getIcono());
	}
	
	public List<DatosClimaticoResponse> toResponseList(List<DatosClimaticos> lista) {
		return lista.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
}
