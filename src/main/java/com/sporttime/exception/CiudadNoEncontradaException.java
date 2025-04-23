package com.sporttime.exception;

public class CiudadNoEncontradaException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public CiudadNoEncontradaException(String nombreCiudad) {
        super("La ciudad '" + nombreCiudad + "' no fue encontrada. Verifica el nombre e intenta nuevamente.");
    }

    public CiudadNoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}
