package com.entrenamiento.appbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.model.AccountMovements;
import com.entrenamiento.appbackend.service.CheckingAccountService;

@RestController
@RequestMapping("/account")
public class CheckingAccountController {
	
	private final CheckingAccountService checkingAccountService;
	
	public CheckingAccountController(CheckingAccountService checkingAccountService) {
		this.checkingAccountService = checkingAccountService;
	}

	@PostMapping("/{id}/{amount}")
	public ResponseEntity<?> loadBalanceAccount(@PathVariable("id") Long id, @PathVariable("amount") double amount) {
		return this.checkingAccountService.loadBalance(id, amount);
	}
	
	@GetMapping("/{id}/movements")
	public ResponseEntity<List<AccountMovements>> obtainMovementsAccount(@PathVariable("id") Long id) {
		return this.checkingAccountService.obtainMovements(id);
	}
}
