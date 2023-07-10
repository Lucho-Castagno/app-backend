package com.entrenamiento.appbackend.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.exception.AppRequestException;
import com.entrenamiento.appbackend.model.Usuario;
import com.entrenamiento.appbackend.repository.UsuarioRepository;

@Service
public class LoginService {
	
	private final UsuarioRepository usuarioRepository;
	
	public LoginService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	public ResponseEntity<?> login(String celular, String contraseña) {
		
		Optional<Usuario> usuario = usuarioRepository.findByCelular(celular);
		
		if (usuario.isEmpty() || !usuario.get().getContraseña().equals(contraseña)) {
			throw new AppRequestException("El usuario y/o contraseña es incorrecto.");
		}
		
		return ResponseEntity.ok(usuario.get());
		
	}
	
}
