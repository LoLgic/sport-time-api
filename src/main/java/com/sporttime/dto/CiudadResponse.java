package com.sporttime.dto;

import java.time.LocalDate;

public record CiudadResponse(Long id, String nombre, String diaSemana, LocalDate fecha) {

}
