package com.sporttime.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sporttime.dto.ErrorResponse;
import com.sporttime.util.ErrorCatalog;

import jakarta.validation.ConstraintViolationException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	// 404 - Ciudad no encontrada
	@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CiudadNoEncontradaException.class)
    public ErrorResponse handleCiudadNoEncontrada(CiudadNoEncontradaException ex) {
        return ErrorResponse.fromCatalog(
        		ErrorCatalog.CIUDAD_NOT_FOUND,
        		HttpStatus.NOT_FOUND,
        		List.of(ex.getMessage()));
    }
	
    // 400 - Argumento ilegal (por ejemplo, ciudad vacía desde query params o lógica de servicio)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        return ErrorResponse.fromCatalog(
            ErrorCatalog.INVALID_DATA,
            HttpStatus.BAD_REQUEST,
            List.of(ex.getMessage())
        );
    }
	
    // 400 - Datos inválidos en el cuerpo de la solicitud (validación de @Valid en DTOs)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.toList());
		
		return ErrorResponse.fromCatalog(
				ErrorCatalog.INVALID_DATA,
				HttpStatus.BAD_REQUEST,
				errors);
	}
	
	// 400 - Validación de parámetros en rutas o servicios (por ejemplo, @Validated en parámetros individuales)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
            .stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .toList();

        return ErrorResponse.fromCatalog(
        		ErrorCatalog.INVALID_DATA, 
        		HttpStatus.BAD_REQUEST, 
        		errors);
    }

	 // 503 - Servicio externo no disponible
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceUnavailableException.class)
    public ErrorResponse handleServiceUnavailable(ServiceUnavailableException ex) {
        return ErrorResponse.fromCatalog(
        		ErrorCatalog.SERVICE_UNAVAILABLE,
        		HttpStatus.SERVICE_UNAVAILABLE,
        		List.of(ex.getMessage()));
    }
	
	// Error Genérico (500)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleInternalServeError(Exception ex) {
        return ErrorResponse.fromCatalog(
        		ErrorCatalog.GENERIC_ERROR,
        		HttpStatus.INTERNAL_SERVER_ERROR,
        		List.of("Ocurrió un error inesperado"));
    }
 }
