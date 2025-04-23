package com.sporttime.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "datos_climaticos")
public class DatosClimaticos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne // Evitamos CascadeType.ALL para prevenir inserciones no deseadas
	@JoinColumn(name = "ciudad_id", nullable = false)
	private CiudadClima ciudad;
	
	private Double agvTemperaturaDiaria;
	private Double temperaturaMax;
	private Double temperaturaMin;
	private Double agvVelocidadViento;
	private Double agvHumedad;
	
	@Embedded
    private CondicionClimatica condicionClimatica;
	
	@ManyToOne
	@JoinColumn(name = "deporte_id")
	private Deporte deporteRecomendado;

	@Column(name = "porcentaje_idoneidad")
	private int porcentajeIdoneidad;
}
