package com.sporttime.dto;

public record DatosClimaticoResponse(
		CiudadResponse ciudadResponse,
        Double agvTemperaturaDiaria,
        Double temperaturaMax,
        Double temperaturaMin,
        Double agvVelocidadViento,
        Double agvHumedad,
        ClimaVisualResponse clima,
        DeporteResponse deporteResponse,
        int porcentajeIdoneidad) {
}
