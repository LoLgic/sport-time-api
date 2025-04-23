package com.sporttime.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.sporttime.util.ErrorCatalog;

import lombok.Builder;

@Builder
public record ErrorResponse(
		String code,
        HttpStatus status,
        String message,
        List<String> detailMessages,
        LocalDateTime timeStamp) {
	
	public static ErrorResponse fromCatalog(ErrorCatalog catalog, HttpStatus status, List<String> details) {
        return ErrorResponse.builder()
                .code(catalog.getCode())
                .status(status)
                .message(catalog.getMessage())
                .detailMessages(details)
                .timeStamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse fromMessage(String code, HttpStatus status, String message, List<String> details) {
        return ErrorResponse.builder()
                .code(code)
                .status(status)
                .message(message)
                .detailMessages(details)
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
