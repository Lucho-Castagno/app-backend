package com.entrenamiento.appbackend.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.model.Usuario;
import com.entrenamiento.appbackend.repository.UsuarioRepository;

@Service
public class LoginService {
	
	private final UsuarioRepository usuarioRepository;
	
	public LoginService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	public ResponseEntity<Usuario> login(String celular, String contraseña) {
		
		Optional<Usuario> usuario = usuarioRepository.findByCelular(celular);
		
		if (usuario.isEmpty() || !usuario.get().getContraseña().equals(contraseña)) {
			return ResponseEntity.badRequest().body(null);
		}
		
		return ResponseEntity.ok(usuario.get());
		
	}
	
}
