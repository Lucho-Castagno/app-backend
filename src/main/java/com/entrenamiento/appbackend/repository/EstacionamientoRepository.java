package com.entrenamiento.appbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entrenamiento.appbackend.model.Estacionamiento;
import com.entrenamiento.appbackend.model.Usuario;

@Repository
public interface EstacionamientoRepository extends JpaRepository<Estacionamiento, Long>{

	boolean existsByUsuarioCelularAndFinIsNull(String celular);

}
