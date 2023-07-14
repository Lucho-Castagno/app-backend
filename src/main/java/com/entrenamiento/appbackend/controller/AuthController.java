package com.entrenamiento.appbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.data.AuthRequest;
import com.entrenamiento.appbackend.data.RegisterRequest;
import com.entrenamiento.appbackend.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> iniciarSesion(@RequestBody AuthRequest request) {
		
		return this.authService.iniciarSesion(request);
		
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> registrarse(@RequestBody RegisterRequest request) {
		return this.authService.registrarse(request);
	}

}
