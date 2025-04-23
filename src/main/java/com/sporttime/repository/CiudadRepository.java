package com.sporttime.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sporttime.model.CiudadClima;

@Repository
public interface CiudadRepository extends JpaRepository<CiudadClima, Long>{
	
	List<CiudadClima> findByNombre(String nombre);
    boolean existsByNombreAndFecha(String nombre, LocalDate fecha);
}


//CiudadClima findByNombreAndFecha(String ciudad, LocalDate fecha);
//void deleteByCiudad(String nombre);