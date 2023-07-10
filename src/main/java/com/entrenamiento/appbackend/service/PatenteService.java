package com.entrenamiento.appbackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.exception.AppRequestException;
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

	public ResponseEntity<List<Patente>> patentes() {
		return ResponseEntity.ok(this.patenteRepository.findAll());
	}

	public ResponseEntity<?> crearPatente(String celular, String cadena) {
		
		if (!validarCadenaPatente(cadena)) {
			throw new AppRequestException("Formato de patente: AAA000 ó AA000AA.");
		}
		
		Optional<Usuario> usuario = usuarioRepository.findByCelular(celular);
		
		if (usuario.isEmpty()) {
			throw new AppRequestException("No se encontro al usuario.");
		}
		
		// este for es para verificar si la patente ya esta asociada al usuario.
		for (Patente patente : usuario.get().getPatentes()) {
			if (patente.getCadena().equals(cadena)) {
				throw new AppRequestException("La patente ya esta asociada a este usuario.");
			}
		}
		
		// verificamos si la patente ya existe, si existe no la creamos de vuelta sino que asociamos la patente con
		// el usuario, en cambio, si no existe la creamos y asociamos con el usuario.
		String formatoCadena = cadena.toUpperCase();
		
		Optional<Patente> patente = patenteRepository.findByCadena(formatoCadena);
		
		if (patente.isEmpty()) {
			Patente patenteNueva = new Patente(formatoCadena);
			usuario.get().addPatente(patenteNueva);
			
			this.usuarioRepository.save(usuario.get());
			
			return ResponseEntity.ok(patenteNueva);
		} else {
			usuario.get().addPatente(patente.get());
			
			this.usuarioRepository.save(usuario.get());
			
			return ResponseEntity.ok(patente.get());
		}
		
		
	}
	
	private boolean validarCadenaPatente(String cadena) {
		String patron = "^(?i)[A-Za-z]{3}\\d{3}$|^(?i)[A-Za-z]{2}\\d{3}[A-Za-z]{2}$";
		return cadena.matches(patron);
	}
	
}
