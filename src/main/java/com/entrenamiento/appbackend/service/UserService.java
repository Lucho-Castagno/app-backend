package com.entrenamiento.appbackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.model.CheckingAccount;
import com.entrenamiento.appbackend.model.Plate;
import com.entrenamiento.appbackend.model.Usser;
import com.entrenamiento.appbackend.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public ResponseEntity<List<Usser>> ussers() {
		return ResponseEntity.ok(this.userRepository.findAll());
	}

	public ResponseEntity<Optional<Usser>> obtainUser(Long id) {
		return ResponseEntity.ok(this.userRepository.findById(id));
	}

	public ResponseEntity<List<Plate>> obtainUserPlates(Long id) {
		Optional<Usser> usser = userRepository.findById(id);
		return ResponseEntity.ok(usser.get().getPlates());
	}
	
	public ResponseEntity<CheckingAccount> obtainUserAccount(Long id) {
		Optional<Usser> usser = userRepository.findById(id);
		return ResponseEntity.ok(usser.get().getAccount());
	}
	
}
