package com.entrenamiento.appbackend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.model.CheckingAccount;
import com.entrenamiento.appbackend.model.Plate;
import com.entrenamiento.appbackend.model.Usser;
import com.entrenamiento.appbackend.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("")
	public ResponseEntity<List<Usser>> obtainUsers() {
		return userService.ussers();
	}
	
	@GetMapping("/{id}/plates")
	public ResponseEntity<List<Plate>> obtainUserPlates(@PathVariable("id") Long id) {
		return userService.obtainUserPlates(id);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Usser>> obtainUser(@PathVariable("id") Long id) {
		return userService.obtainUser(id);
	}
	
	@GetMapping("/{id}/account")
	public ResponseEntity<CheckingAccount> obtainUserAccount(@PathVariable("id") Long id) {
		return this.userService.obtainUserAccount(id);
	}
	
}
