package com.sporttime.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sporttime.dto.DatosClimaticoResponse;
import com.sporttime.service.ClimaService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clima")
@RequiredArgsConstructor
public class ClimaController {

	private final ClimaService climaService;

	@GetMapping("/{ciudad}")
	public ResponseEntity<List<DatosClimaticoResponse>> getDatosClimaticosPorCiudad(
			@PathVariable @NotBlank @Size(min = 2, max = 100) String ciudad) {
		return ResponseEntity.ok(climaService.obtenerDatosClimaticos(ciudad));
	}
}
