package com.sporttime.service;

public interface RecomendacionDeportivaService {
	record Recomendacion(String deporte, int porcentaje) {}
	
	Recomendacion calcularRecomendacion(double temperatura, double viento, double humedad);
}
