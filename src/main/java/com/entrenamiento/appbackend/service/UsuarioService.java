package com.entrenamiento.appbackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.model.CtaCorriente;
import com.entrenamiento.appbackend.model.Patente;
import com.entrenamiento.appbackend.model.Usuario;
import com.entrenamiento.appbackend.repository.CtaCorrienteRepository;
import com.entrenamiento.appbackend.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	private final UsuarioRepository usuarioRepository;
	private final CtaCorrienteRepository ctaCorrienteRepository;
	
	public UsuarioService(UsuarioRepository usuarioRepository, CtaCorrienteRepository ctaCorrienteRepository) {
		this.usuarioRepository = usuarioRepository;
		this.ctaCorrienteRepository = ctaCorrienteRepository;
	}
	
	public ResponseEntity<String> crearUsuario(Usuario usuario) {
		
		Optional<Usuario> usuarioExiste = this.usuarioRepository.findByCelular(usuario.getCelular()); 
		if (usuarioExiste.isEmpty()) {
			CtaCorriente ctaCorriente = new CtaCorriente();
			this.ctaCorrienteRepository.save(ctaCorriente);
			
			usuario.setCtaCorriente(ctaCorriente);
			this.usuarioRepository.save(usuario);
			
			return ResponseEntity.ok().body("Usuario registrado con exito!");
		}
		
		return ResponseEntity.badRequest().body("El celular que intenta ingresar ya esta registrado en el sistema.");
 		
	}

	public List<Usuario> usuarios() {
		return this.usuarioRepository.findAll();
	}

	public Optional<Usuario> usuarioPorId(String celular) {
		return this.usuarioRepository.findById(celular);
	}

	public ResponseEntity<List<Patente>> obtenerPatentesUsuario(String celular) {
		Optional<Usuario> usuario = usuarioRepository.findByCelular(celular);
		if (usuario.isPresent()) {
			return ResponseEntity.ok().body(usuario.get().getPatentes());
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
}
