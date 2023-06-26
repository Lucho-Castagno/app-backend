package com.entrenamiento.appbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entrenamiento.appbackend.model.CtaCorriente;

public interface CtaCorrienteRepository extends JpaRepository<CtaCorriente, Long>{
	
	CtaCorriente findByUsuarioCelular(String celular);
	
}

