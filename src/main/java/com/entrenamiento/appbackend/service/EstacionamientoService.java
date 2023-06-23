package com.entrenamiento.appbackend.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.exceptions.EstacionamientoException;
import com.entrenamiento.appbackend.exceptions.EstacionamientoNotFoundException;
import com.entrenamiento.appbackend.exceptions.CtaCorrienteNotFoundException;
import com.entrenamiento.appbackend.model.CtaCorriente;
import com.entrenamiento.appbackend.model.Estacionamiento;
import com.entrenamiento.appbackend.model.Patente;
import com.entrenamiento.appbackend.repository.CtaCorrienteRepository;
import com.entrenamiento.appbackend.repository.EstacionamientoRepository;

@Service
public class EstacionamientoService {
	
	private final EstacionamientoRepository estacionamientoRepository;
	private final CtaCorrienteRepository ctaCorrienteRepository;
	
	public EstacionamientoService(EstacionamientoRepository estacionamientoRepository, CtaCorrienteRepository ctaCorrienteRepository) {
		this.estacionamientoRepository = estacionamientoRepository;
		this.ctaCorrienteRepository = ctaCorrienteRepository;
	}
	
	public List<Estacionamiento> estacionamientos() {
		return this.estacionamientoRepository.findAll();
	}
	
	public Optional<Estacionamiento> estacionamientoPorId(Long id) {
		return this.estacionamientoRepository.findById(id);
	}
	
	public void iniciarEstacionamiento(String celular, Patente patente) {
	
		if (this.estacionamientoRepository.existsByUsuarioCelularAndFinIsNull(celular)) {
			throw new EstacionamientoException("Ya existe un estacionamiento iniciado por este usuario.");
		}
		
		CtaCorriente ctaCorriente = this.ctaCorrienteRepository.findByUsuarioCelular(celular);
		if (ctaCorriente == null) {
			throw new CtaCorrienteNotFoundException("Cuenta Corriente no encontrada.");
		}
		if (ctaCorriente.getSaldo() < 10.0) {
			throw new EstacionamientoException("Saldo insuficiente para iniciar un estacionamiento.");
		}
		
		Estacionamiento estacionamiento = new Estacionamiento();
		estacionamiento.setUsuario(ctaCorriente.getUsuario());
		estacionamiento.setPatente(patente);
		
		estacionamientoRepository.save(estacionamiento);
		
	}
	
	public void finalizarEstacionamiento(Long id) {
		
		Estacionamiento estacionamiento = this.estacionamientoRepository.findById(id).orElseThrow(() -> new EstacionamientoNotFoundException("No se encontro el estacionamiento indicado."));
		
		if (estacionamiento.getFin() != null) {
			throw new EstacionamientoException("El estacionamiento indicado ya finalizó.");
		}
		
		// calculo de horas para el importe total
		Long horasTotales = ChronoUnit.HOURS.between(estacionamiento.getInicio(), estacionamiento.getFin());
		double importeTotal = 10.0 * horasTotales;
		
		CtaCorriente ctaCorriente = this.ctaCorrienteRepository.findByUsuarioCelular(estacionamiento.getUsuario().getCelular());
		double saldoNuevo = ctaCorriente.getSaldo() - importeTotal;
		ctaCorriente.setSaldo(saldoNuevo);
		ctaCorrienteRepository.save(ctaCorriente);
		
		estacionamiento.setFin(LocalDateTime.now());
		estacionamiento.setImporte(importeTotal);
		estacionamientoRepository.save(estacionamiento);
		
	}
	
}
