package com.entrenamiento.appbackend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.MovementType;
import com.entrenamiento.appbackend.exception.AppRequestException;
import com.entrenamiento.appbackend.model.CheckingAccount;
import com.entrenamiento.appbackend.model.AccountMovements;
import com.entrenamiento.appbackend.repository.CheckingAccountRepository;
import com.entrenamiento.appbackend.repository.AccountMovementsRepository;

@Service
public class CheckingAccountService {
	
	private final CheckingAccountRepository checkingAccountRepository;
	private final AccountMovementsRepository accountMovementsRepository;
	
	public CheckingAccountService(CheckingAccountRepository checkingAccountRepository, AccountMovementsRepository accountMovementsRepository) {
		this.checkingAccountRepository = checkingAccountRepository;
		this.accountMovementsRepository = accountMovementsRepository;
	}
	
	public ResponseEntity<?> loadBalance(Long id, double amount) {
		
		Optional<CheckingAccount> account = this.checkingAccountRepository.findById(id);
		if(account.isEmpty()) {
			throw new AppRequestException("No se encontro la cuenta.");
		}
		
		if (amount < 100.0) {
			throw new AppRequestException("El monto minimo es de $100.0 pesos.");
		}
		
		AccountMovements movement = new AccountMovements();
		movement.setDate(LocalDateTime.now());
		movement.setType(MovementType.CHARGE.getDescription());
		movement.setAmount(amount);
		
		CheckingAccount checkingAccount = account.get();
		checkingAccount.charge(amount);
		
		checkingAccount.addMovements(movement);
		this.checkingAccountRepository.save(checkingAccount);
		
		movement.setAccount(checkingAccount);
		this.accountMovementsRepository.save(movement);
		
		return ResponseEntity.ok(checkingAccount);
		
	}
	
	public void performTransaction(Long id, double amount) {
		CheckingAccount checkingAccount = this.findUserAccount(id);
		checkingAccount.transaction(amount);
		
		AccountMovements movement = new AccountMovements();
		movement.setDate(LocalDateTime.now());
		movement.setType(MovementType.TRANSACTION.getDescription());
		movement.setAmount(amount);
		
		checkingAccount.addMovements(movement);
		this.checkingAccountRepository.save(checkingAccount);
		
		movement.setAccount(checkingAccount);
		this.accountMovementsRepository.save(movement);
	}
	
	public CheckingAccount findUserAccount(Long id) {
		return this.checkingAccountRepository.findByUserId(id);
	}

	public ResponseEntity<List<AccountMovements>> obtainMovements(Long id) {
		Optional<CheckingAccount> account = this.checkingAccountRepository.findById(id);
		
		return ResponseEntity.ok(account.get().getAccountMovements());
		
	}
	
}