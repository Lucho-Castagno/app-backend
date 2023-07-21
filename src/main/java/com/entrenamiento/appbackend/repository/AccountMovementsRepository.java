package com.entrenamiento.appbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entrenamiento.appbackend.model.AccountMovements;

public interface AccountMovementsRepository extends JpaRepository<AccountMovements, Long> {

}
