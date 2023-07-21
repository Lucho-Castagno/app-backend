package com.entrenamiento.appbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entrenamiento.appbackend.model.CheckingAccount;

public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long>{
	
	CheckingAccount findByUserId(Long id);
	
}

