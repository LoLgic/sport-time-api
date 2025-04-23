package com.sporttime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sporttime.model.DatosClimaticos;

@Repository
public interface DatosClimaticosRepository extends JpaRepository<DatosClimaticos, Long> {
	//List<DatosClimaticos> findByCiudad_NombreAndCiudad_Fecha(String nombreCiudad, LocalDate fecha);
	List<DatosClimaticos> findByCiudadNombre(String ciudad);
}

//boolean existsByCiudad_NombreAndCiudad_Fecha(String nombreCiudad, LocalDate fecha);

//void deleteByCiudad(CiudadClima ciudad);

//List<DatosClimaticos> findByCiudadNombreAndCiudadFecha(String nombre,LocalDate fecha);