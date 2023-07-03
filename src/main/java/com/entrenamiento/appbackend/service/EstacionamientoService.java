package com.entrenamiento.appbackend.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
	
	public static final LocalTime HORARIO_APERTURA = LocalTime.of(8, 0);
	public static final LocalTime HORARIO_CIERRE = LocalTime.of(20, 0);
	
	public EstacionamientoService(EstacionamientoRepository estacionamientoRepository, CtaCorrienteRepository ctaCorrienteRepository, PatenteRepository patenteRepository) {
		this.estacionamientoRepository = estacionamientoRepository;
		this.ctaCorrienteRepository = ctaCorrienteRepository;
		this.patenteRepository = patenteRepository;
	}
	
	public ResponseEntity<List<Estacionamiento>> estacionamientos() {
		return ResponseEntity.ok(this.estacionamientoRepository.findAll());
	}
	
	public ResponseEntity<String> iniciarEstacionamiento(String celular, String cadena) {
		
		LocalDateTime inicio = LocalDateTime.now();
		if (inicio.toLocalTime().isBefore(HORARIO_APERTURA)) {
			return ResponseEntity.badRequest().body("El horario de apertura del sistema de estacionamientos es a las 8:00 am");
		}
		
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
		return ResponseEntity.ok("Estacionamiento iniciado!");
		
	}
	
	public ResponseEntity<String> finalizarEstacionamiento(Long id) {
		
		LocalDateTime fin = LocalDateTime.now();
		if (fin.toLocalTime().isAfter(HORARIO_CIERRE)) {
			return ResponseEntity.badRequest().body("El horario de cierre del sistema de estacionamientos es a las 20:00 pm");
		}
		
		Optional<Estacionamiento> estacionamientoOpcional = this.estacionamientoRepository.findById(id);
		
		if (estacionamientoOpcional.isEmpty()) {
			return ResponseEntity.badRequest().body("No se encontro el estacionamiento indicado.");
		}
		
		Estacionamiento estacionamiento = estacionamientoOpcional.get();
		
		if (estacionamiento.getFin() != null) {
			return ResponseEntity.badRequest().body("El estacionamiento indicado ya finalizó.");
		}
		
		long horasTotales = calculoHorasEstacionamiento(estacionamiento.getInicio(), LocalDateTime.now());
		double importeTotal = 10.0 * horasTotales;
		
		CtaCorriente ctaCorriente = this.ctaCorrienteRepository.findByUsuarioCelular(estacionamiento.getUsuario().getCelular());
		ctaCorriente.consumo(importeTotal);
		ctaCorrienteRepository.save(ctaCorriente);
		
		estacionamiento.setFin(LocalDateTime.now());
		estacionamiento.setImporte(importeTotal);
		estacionamientoRepository.save(estacionamiento);
		
		return ResponseEntity.ok("Estacionamiento finalizado!");
		
	}
	
	public long calculoHorasEstacionamiento(LocalDateTime inicio, LocalDateTime fin) {
		
		long horas = 0;
		
		Duration duracion = Duration.between(inicio, fin);
		long minutos = duracion.toMinutesPart();
		// para el caso en el que el estacionamiento inicie y termine en el mismo dia
		if (inicio.toLocalDate().equals(fin.toLocalDate())) {
			
			horas = (long) Math.ceil(duracion.toMinutes() / 60);
			
		} else {
			
			long dias = duracion.toDays();
			
			Duration duracionPrimerDia = Duration.between(inicio.toLocalTime(), HORARIO_CIERRE);
			Duration duracionUltimoDia = Duration.between(HORARIO_APERTURA, fin.toLocalTime());
			
			if ( dias <= 1) {
				
				horas = (long) Math.ceil((duracionPrimerDia.toMinutes() + duracionUltimoDia.toMinutes()) / 60);
			
			} else {
				
				long diasCompletos = (fin.getDayOfMonth() - inicio.getDayOfMonth()) + 1;
				long diasIntermedios = diasCompletos - 2;
				long horasCompletas = diasIntermedios * (HORARIO_CIERRE.getHour() - HORARIO_APERTURA.getHour());
				
				horas = (long) Math.ceil((duracionPrimerDia.toMinutes() + duracionUltimoDia.toMinutes()) / 60) + horasCompletas;
			}

		}
		
		if (minutos > 0) horas ++;
		
		return horas;
		
	}

	public ResponseEntity<List<Estacionamiento>> estacionamientosUsuario(String celular) {
		return ResponseEntity.ok(this.estacionamientoRepository.findAllByUsuarioCelular(celular));
	}
	
	public ResponseEntity<Optional<Estacionamiento>> estacionamientoPendiente(String celular) {
		return ResponseEntity.ok(this.estacionamientoRepository.findByUsuarioCelularAndFinIsNull(celular));
	}
	
}
