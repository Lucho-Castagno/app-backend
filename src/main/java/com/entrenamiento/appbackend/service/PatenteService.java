package com.entrenamiento.appbackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.model.Patente;
import com.entrenamiento.appbackend.model.Usuario;
import com.entrenamiento.appbackend.repository.PatenteRepository;
import com.entrenamiento.appbackend.repository.UsuarioRepository;

@Service
public class PatenteService {
	
	private final PatenteRepository patenteRepository;
	private final UsuarioRepository usuarioRepository;
	
	public PatenteService(PatenteRepository patenteRepository, UsuarioRepository usuarioRepository) {
		this.patenteRepository = patenteRepository;
		this.usuarioRepository = usuarioRepository;
	}

	public List<Patente> patentes() {
		return this.patenteRepository.findAll();
	}

	public Patente crearPatente(String cadena, Usuario usuario) {
		
		Patente patente = new Patente(cadena);
		this.patenteRepository.save(patente);
		
		usuario.addPatente(patente);
		this.usuarioRepository.save(usuario);
		
		return patente;
	}
	
}
