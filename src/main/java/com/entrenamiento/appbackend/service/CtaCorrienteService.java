package com.entrenamiento.appbackend.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.model.CtaCorriente;
import com.entrenamiento.appbackend.repository.CtaCorrienteRepository;

@Service
public class CtaCorrienteService {
	
	private final CtaCorrienteRepository ctaCorrienteRepository;
	
	public CtaCorrienteService(CtaCorrienteRepository ctaCorrienteRepository) {
		this.ctaCorrienteRepository = ctaCorrienteRepository;
	}
	
	public ResponseEntity<?> cargarSaldo(Long id, double monto) {
		
		Optional<CtaCorriente> cuenta = this.ctaCorrienteRepository.findById(id);
		if(cuenta.isEmpty()) {
			return ResponseEntity.badRequest().body("No se encontro la cuenta.");
		}
		
		CtaCorriente ctaCorriente = cuenta.get();
		ctaCorriente.cargarCredito(monto);
		this.ctaCorrienteRepository.save(ctaCorriente);
		
		return ResponseEntity.ok(ctaCorriente);
		
	}
	
}
