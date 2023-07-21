package com.entrenamiento.appbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entrenamiento.appbackend.model.Plate;

public interface PlateRepository extends JpaRepository<Plate, Long> {

	Optional<Plate> findByPlate(String plate);
	
}