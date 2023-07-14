package com.entrenamiento.appbackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.model.CtaCorriente;
import com.entrenamiento.appbackend.model.Patente;
import com.entrenamiento.appbackend.model.Usuario;
import com.entrenamiento.appbackend.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	private final UsuarioRepository usuarioRepository;
	
	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public ResponseEntity<List<Usuario>> usuarios() {
		return ResponseEntity.ok(this.usuarioRepository.findAll());
	}

	public ResponseEntity<Optional<Usuario>> usuarioPorId(String celular) {
		return ResponseEntity.ok(this.usuarioRepository.findById(celular));
	}

	public ResponseEntity<List<Patente>> obtenerPatentesUsuario(String celular) {
		Optional<Usuario> usuario = usuarioRepository.findByCelular(celular);
		return ResponseEntity.ok(usuario.get().getPatentes());
	}
	
	public ResponseEntity<CtaCorriente> obtenerCuentaUsuario(String celular) {
		Optional<Usuario> usuario = usuarioRepository.findByCelular(celular);
		return ResponseEntity.ok(usuario.get().getCtaCorriente());
	}
	
}
