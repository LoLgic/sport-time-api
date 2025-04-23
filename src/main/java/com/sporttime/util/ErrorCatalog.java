package com.sporttime.util;

import lombok.Getter;

@Getter
public enum ErrorCatalog {

	CIUDAD_NOT_FOUND("ERR_CLIMA_001", "Datos no disponibles para la ciudad"),
    INVALID_DATA("ERR_CLIMA_002", "El nombre de la ciudad no puede estar vacío"),
    SERVICE_UNAVAILABLE("ERR_CLIMA_003", "Ingrese un nombre de ciudad valido"),
    GENERIC_ERROR("ERR_GEN_001", "Ingrese un nombre de una ciudad");

    private final String code;
    private final String message;

    ErrorCatalog(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
