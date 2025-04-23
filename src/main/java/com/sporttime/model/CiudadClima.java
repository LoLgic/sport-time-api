package com.sporttime.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "ciudades", uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"nombre", "fecha"})
	})
public class CiudadClima {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String nombre;

	@Column(name = "dia_semana")
	private String diaSemana;

	private LocalDate fecha;
}
