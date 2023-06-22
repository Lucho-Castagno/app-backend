package com.entrenamiento.appbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entrenamiento.appbackend.model.CtaCorriente;

@Repository
public interface CtaCorrienteRepository extends JpaRepository<CtaCorriente, Long>{

}

