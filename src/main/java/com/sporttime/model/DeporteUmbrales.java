package com.sporttime.model;

import com.google.common.collect.Range;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeporteUmbrales {
	
	private final String nombre;
	
	private final Range<Double> tempOptima;
	private final Range<Double> tempAceptable;
	private final Range<Double> vientoOptimo;
	private final Range<Double> vientoAceptable;
	private final Range<Double> humedadOptima;
	private final Range<Double> humedadAceptable;


	// Método para calcular puntuación
	public int calcularPuntuacion(double temp, double viento, double humedad) {
		int puntos = 0;

		// Temperatura
		if (tempOptima.contains(temp))puntos += 2;
		else if (tempAceptable.contains(temp))puntos += 1;

		// Viento (excepto natación)
		if (!nombre.equals("Natación")) {
			if (vientoOptimo.contains(viento))puntos += 2;
			else if (vientoAceptable.contains(viento))puntos += 1;
		}

		// Humedad
		if (humedadOptima.contains(humedad))puntos += 2;
		else if (humedadAceptable.contains(humedad))puntos += 1;

		return puntos;
	}
}
