package com.entrenamiento.appbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entrenamiento.appbackend.model.Patente;
import com.entrenamiento.appbackend.model.Usuario;

@Repository
public interface PatenteRepository extends JpaRepository<Patente, Long> {

}