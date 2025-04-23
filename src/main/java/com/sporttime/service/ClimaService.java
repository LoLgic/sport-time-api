package com.sporttime.service;

import java.util.List;

import com.sporttime.dto.DatosClimaticoResponse;

public interface ClimaService {

	List<DatosClimaticoResponse> obtenerDatosClimaticos(String ciudad);
}
