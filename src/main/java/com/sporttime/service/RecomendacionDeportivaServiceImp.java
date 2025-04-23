package com.sporttime.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Range;
import com.sporttime.model.DeporteUmbrales;


@Service
public class RecomendacionDeportivaServiceImp implements RecomendacionDeportivaService{

	private final List<DeporteUmbrales> deportes = List.of(
			// Fútbol
	        new DeporteUmbrales("Fútbol", 
	            Range.closed(15.0, 25.0),  // Temp óptima
	            Range.closed(10.0, 30.0),  // Temp aceptable (10-14 y 26-30)
	            Range.closed(0.0, 20.0),   // Viento óptimo
	            Range.closed(0.0, 30.0),   // Viento aceptable (21-30)
	            Range.closed(40.0, 70.0),  // Humedad óptima
	            Range.closed(30.0, 80.0)), // Humedad aceptable (30-39 y 71-80)
	            
	        // Baloncesto
	        new DeporteUmbrales("Baloncesto",
	            Range.closed(18.0, 25.0),
	            Range.closed(12.0, 30.0),
	            Range.closed(0.0, 10.0),
	            Range.closed(0.0, 15.0),
	            Range.closed(40.0, 60.0),
	            Range.closed(30.0, 75.0)),
	            
	            
	        // Tenis
	        new DeporteUmbrales("Tenis",
	            Range.closed(15.0, 25.0),
	            Range.closed(10.0, 32.0),
	            Range.closed(0.0, 15.0),
	            Range.closed(0.0, 25.0),
	            Range.closed(30.0, 60.0),
	            Range.closed(20.0, 75.0)),
	            
	        // Atletismo
	        new DeporteUmbrales("Atletismo",
	            Range.closed(10.0, 22.0),
	            Range.closed(5.0, 28.0),
	            Range.closed(0.0, 20.0),
	            Range.closed(0.0, 30.0),
	            Range.closed(30.0, 60.0),
	            Range.closed(20.0, 75.0)),
	            
	        // Natación (sin viento)
	        new DeporteUmbrales("Natación",
	            Range.closed(25.0, 35.0),
	            Range.closed(20.0, 40.0),
	            Range.closedOpen(0.0, Double.MAX_VALUE), // No aplica
	            Range.closedOpen(0.0, Double.MAX_VALUE), // No aplica
	            Range.closed(50.0, 90.0),
	            Range.closed(40.0, 95.0)),
	            
	        // Ciclismo
	        new DeporteUmbrales("Ciclismo",
	            Range.closed(10.0, 22.0),
	            Range.closed(5.0, 28.0),
	            Range.closed(0.0, 15.0),
	            Range.closed(0.0, 25.0),
	            Range.closed(30.0, 70.0),
	            Range.closed(20.0, 80.0)),
	            
	        // Béisbol
	        new DeporteUmbrales("Béisbol",
	            Range.closed(18.0, 28.0),
	            Range.closed(12.0, 35.0),
	            Range.closed(0.0, 20.0),
	            Range.closed(0.0, 30.0),
	            Range.closed(30.0, 65.0),
	            Range.closed(20.0, 80.0)),
	            
	            
	        // Golf
	        new DeporteUmbrales("Golf",
	            Range.closed(15.0, 30.0),
	            Range.closed(10.0, 35.0),
	            Range.closed(0.0, 15.0),
	            Range.closed(0.0, 25.0),
	            Range.closed(30.0, 65.0),
	            Range.closed(20.0, 80.0))
	    );

	@Override
	public Recomendacion calcularRecomendacion(double temp, double viento, double humedad) {
		Map<String, Integer> puntuaciones = new HashMap<>();

		// Calcular puntuación para cada deporte
		deportes.forEach(deporte -> {
			int puntos = deporte.calcularPuntuacion(temp, viento, humedad);
			puntuaciones.put(deporte.getNombre(), puntos);
		});

		// Obtener deporte con mayor puntuación
		String deporteRecomendado = Collections.max(puntuaciones.entrySet(), 
				Map.Entry.comparingByValue()).getKey();

		// Calcular porcentaje de idoneidad
		int porcentaje = calcularPorcentajeIdoneidad(temp, viento, humedad, deporteRecomendado);
		
	
		return new Recomendacion(deporteRecomendado, (int) Math.round(porcentaje));
	}

	private int calcularPorcentajeIdoneidad(double temp, double viento, double humedad, String deporte) {
		DeporteUmbrales umbral = deportes.stream()
				.filter(d -> d.getNombre().equals(deporte))
				.findFirst()
				.orElseThrow();

		int puntosOptimos = 0;
		int puntosTotales = 6; // 2 puntos por cada factor (temp, viento, humedad)

		if (umbral.getTempOptima().contains(temp))
			puntosOptimos += 2;
		else if (umbral.getTempAceptable().contains(temp))
			puntosOptimos += 1;

		if (umbral.getVientoOptimo().contains(viento))
			puntosOptimos += 2;
		else if (umbral.getVientoAceptable().contains(viento))
			puntosOptimos += 1;

		if (umbral.getHumedadOptima().contains(humedad))
			puntosOptimos += 2;
		else if (umbral.getHumedadAceptable().contains(humedad))
			puntosOptimos += 1;

		return (int) Math.round((puntosOptimos * 100.0) / puntosTotales);
	}
}
