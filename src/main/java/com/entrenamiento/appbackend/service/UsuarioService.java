package com.entrenamiento.appbackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.exceptions.UsuarioNotFoundException;
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
	
	public Usuario crearUsuario(Usuario usuario) {
		CtaCorriente ctaCorriente = new CtaCorriente();
		this.ctaCorrienteRepository.save(ctaCorriente);
		
		usuario.setCtaCorriente(ctaCorriente);
		
		return this.usuarioRepository.save(usuario);
	}

	public List<Usuario> usuarios() {
		return this.usuarioRepository.findAll();
	}

	public Optional<Usuario> usuarioPorId(String celular) {
		return this.usuarioRepository.findById(celular);
	}

	public List<Patente> obtenerPatentesUsuario(String celular) {
		Optional<Usuario> usuario = usuarioRepository.findByCelular(celular);
		if (usuario.isPresent()) {
			return (List<Patente>) usuario.get().getPatentes();
		} else {
			throw new UsuarioNotFoundException("No se encontro al usuario.");
		}
	}
	
}
