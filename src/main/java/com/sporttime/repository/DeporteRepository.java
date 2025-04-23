package com.sporttime.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sporttime.model.Deporte;

@Repository
public interface DeporteRepository extends JpaRepository<Deporte, Long>{

	Optional<Deporte> findByNombre(String nombre);
}
