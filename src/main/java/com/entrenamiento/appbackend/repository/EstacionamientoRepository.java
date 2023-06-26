package com.entrenamiento.appbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entrenamiento.appbackend.model.Estacionamiento;

public interface EstacionamientoRepository extends JpaRepository<Estacionamiento, Long>{

	boolean existsByUsuarioCelularAndFinIsNull(String celular);

}
