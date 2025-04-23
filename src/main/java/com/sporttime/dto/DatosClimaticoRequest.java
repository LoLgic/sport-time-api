package com.sporttime.dto;

import jakarta.validation.constraints.NotBlank;

public record DatosClimaticoRequest(
		// Anotación que valida que el campo no sea nulo ni vacío
        // Si la validación falla, muestra el mensaje de error especificado
		@NotBlank(message = "El nombre de la ciudad es obligatorio.")
		String ciudad
		) {
}
