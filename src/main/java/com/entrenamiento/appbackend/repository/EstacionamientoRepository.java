package com.entrenamiento.appbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entrenamiento.appbackend.model.Estacionamiento;

public interface EstacionamientoRepository extends JpaRepository<Estacionamiento, Long>{

	boolean existsByUsuarioCelularAndFinIsNull(String celular);
	
	Optional<Estacionamiento> findByUsuarioCelularAndFinIsNull(String celular);
	
	List<Estacionamiento> findAllByUsuarioCelular(String celular);

	boolean existsByPatenteCadenaAndFinIsNull(String cadena);

}
