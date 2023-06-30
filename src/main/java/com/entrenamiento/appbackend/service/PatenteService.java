package com.entrenamiento.appbackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
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

	public ResponseEntity<Patente> crearPatente(String celular, String cadena) {
		
		if (!validarCadenaPatente(cadena)) {
			return ResponseEntity.badRequest().body(null);
		}
		
		Optional<Usuario> usuario = usuarioRepository.findByCelular(celular);
		
		if (usuario.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		
		// este for es para verificar si la patente ya esta asociada al usuario.
		for (Patente patente : usuario.get().getPatentes()) {
			if (patente.getCadena().equals(cadena)) {
				return ResponseEntity.badRequest().body(null);
			}
		}
		
		// verificamos si la patente ya existe, si existe no la creamos de vuelta sino que asociamos la patente con
		// el usuario, en cambio, si no existe la creamos y asociamos con el usuario.
		
		Optional<Patente> patente = patenteRepository.findByCadena(cadena);
		
		if (patente.isEmpty()) {
			Patente patenteNueva = new Patente(cadena);
			usuario.get().addPatente(patenteNueva);
			
			this.usuarioRepository.save(usuario.get());
			
			return ResponseEntity.ok(patenteNueva);
		} else {
			usuario.get().addPatente(patente.get());
			
			this.usuarioRepository.save(usuario.get());
			
			return ResponseEntity.ok(patente.get());
		}
		
		
	}
	
	public boolean validarCadenaPatente(String cadena) {
		String patron = "^[A-Z]{3}\\d{3}$|^[A-Z]{2}\\d{3}[A-Z]{2}$";
		return cadena.matches(patron);
	}
	
}
