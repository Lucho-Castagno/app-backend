package com.entrenamiento.appbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.model.Usuario;
import com.entrenamiento.appbackend.service.LoginService;

@RestController
public class LoginController {
	
	private final LoginService loginService;
	
	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<Usuario> login(@RequestBody Usuario usuario) {
		
		return this.loginService.login(usuario.getCelular(), usuario.getContraseña());
		
	}

}
