package com.sporttime.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class CondicionClimatica {
	
	private String estado;
    private String descripcion;
    private String icono;
}
