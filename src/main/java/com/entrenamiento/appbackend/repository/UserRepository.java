package com.entrenamiento.appbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entrenamiento.appbackend.model.Usser;

public interface UserRepository extends JpaRepository<Usser, Long>{

	Optional<Usser> findByCellphone(String cellphone);

}
