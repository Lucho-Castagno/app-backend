package com.entrenamiento.appbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entrenamiento.appbackend.model.Patente;

@Repository
public interface PatenteRepository extends JpaRepository<Patente, Long> {

}