package com.entrenamiento.appbackend.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.model.CtaCorriente;
import com.entrenamiento.appbackend.model.Estacionamiento;
import com.entrenamiento.appbackend.model.Patente;
import com.entrenamiento.appbackend.repository.CtaCorrienteRepository;
import com.entrenamiento.appbackend.repository.EstacionamientoRepository;
import com.entrenamiento.appbackend.repository.PatenteRepository;

@Service
public class EstacionamientoService {
	
	private final EstacionamientoRepository estacionamientoRepository;
	private final CtaCorrienteRepository ctaCorrienteRepository;
	private final PatenteRepository patenteRepository;
	
	public EstacionamientoService(EstacionamientoRepository estacionamientoRepository, CtaCorrienteRepository ctaCorrienteRepository, PatenteRepository patenteRepository) {
		this.estacionamientoRepository = estacionamientoRepository;
		this.ctaCorrienteRepository = ctaCorrienteRepository;
		this.patenteRepository = patenteRepository;
	}
	
	public List<Estacionamiento> estacionamientos() {
		return this.estacionamientoRepository.findAll();
	}
	
	public Optional<Estacionamiento> estacionamientoPorId(Long id) {
		return this.estacionamientoRepository.findById(id);
	}
	
	public ResponseEntity<String> iniciarEstacionamiento(String celular, String cadena) {
	
		if (this.estacionamientoRepository.existsByUsuarioCelularAndFinIsNull(celular)) {
			return ResponseEntity.badRequest().body("Ya existe un estacionamiento iniciado por este usuario.");
		}
		
		CtaCorriente ctaCorriente = this.ctaCorrienteRepository.findByUsuarioCelular(celular);
		if (ctaCorriente == null) {
			return ResponseEntity.badRequest().body("Cuenta Corriente no encontrada.");
		}
		if (ctaCorriente.getSaldo() < 10.0) {
			return ResponseEntity.badRequest().body("Saldo insuficiente para iniciar un estacionamiento.");
		}
		
		Estacionamiento estacionamiento = new Estacionamiento();
		estacionamiento.setUsuario(ctaCorriente.getUsuario());
		
		Optional<Patente> patente = patenteRepository.findByCadena(cadena);
		
		estacionamiento.setPatente(patente.get());
		
		estacionamientoRepository.save(estacionamiento);
		return ResponseEntity.ok().body("Estacionamiento iniciado!");
		
	}
	
	public ResponseEntity<String> finalizarEstacionamiento(Long id) {
		
		Optional<Estacionamiento> estacionamientoOpcional = this.estacionamientoRepository.findById(id);
		
		if (estacionamientoOpcional.isEmpty()) {
			return ResponseEntity.badRequest().body("No se encontro el estacionamiento indicado.");
		}
		
		Estacionamiento estacionamiento = estacionamientoOpcional.get();
		
		if (estacionamiento.getFin() != null) {
			return ResponseEntity.badRequest().body("El estacionamiento indicado ya finalizó.");
		}
		
		// calculo de horas para el importe total
		// Duration duracionTiempo = Duration.between(estacionamiento.getInicio(), LocalDateTime.now());
		// duracionTiempo.toHours();
		long horasTotales = calculoHorasEstacionamiento(estacionamiento.getInicio(), LocalDateTime.now());
		double importeTotal = 10.0 * horasTotales;
		
		CtaCorriente ctaCorriente = this.ctaCorrienteRepository.findByUsuarioCelular(estacionamiento.getUsuario().getCelular());
		ctaCorriente.consumo(importeTotal);
		ctaCorrienteRepository.save(ctaCorriente);
		
		estacionamiento.setFin(LocalDateTime.now());
		estacionamiento.setImporte(importeTotal);
		estacionamientoRepository.save(estacionamiento);
		
		return ResponseEntity.ok().body("Estacionamiento finalizado!");
		
	}
	
	public long calculoHorasEstacionamiento(LocalDateTime inicio, LocalDateTime fin) {
		
		// total de horas entre las dos fechas
		long horasTotales = ChronoUnit.HOURS.between(inicio, fin);
		
		int horasFueraHorario = 0;
		
		if (inicio.getHour() > 8) {
			horasFueraHorario += (8 - inicio.getHour());
		}
		
		if (fin.getHour() < 20) {
			horasFueraHorario += (fin.getHour() - 20);
		}
		
		return horasTotales - horasFueraHorario;
		
	}
	
}
