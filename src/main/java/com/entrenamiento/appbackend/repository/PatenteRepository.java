package com.entrenamiento.appbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entrenamiento.appbackend.model.Patente;

public interface PatenteRepository extends JpaRepository<Patente, Long> {

	Optional<Patente> findByCadena(String cadena);
	
}