package com.entrenamiento.appbackend.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.Feriado;
import com.entrenamiento.appbackend.model.CtaCorriente;
import com.entrenamiento.appbackend.model.Estacionamiento;
import com.entrenamiento.appbackend.model.Patente;
import com.entrenamiento.appbackend.repository.EstacionamientoRepository;
import com.entrenamiento.appbackend.repository.PatenteRepository;

@Service
public class EstacionamientoService {
	
	private final EstacionamientoRepository estacionamientoRepository;
	private final CtaCorrienteService ctaCorrienteService;
	private final PatenteRepository patenteRepository;
	
	private static final LocalTime HORARIO_APERTURA = LocalTime.of(8, 0);
	private static final LocalTime HORARIO_CIERRE = LocalTime.of(20, 0);
	private static final Double ESQUEMA_FRACCIONAMIENTO = 15.0;
	private static final Double COSTO_FRACCION = 2.50;
	
	public EstacionamientoService(EstacionamientoRepository estacionamientoRepository, CtaCorrienteService ctaCorrienteService, PatenteRepository patenteRepository) {
		this.estacionamientoRepository = estacionamientoRepository;
		this.ctaCorrienteService = ctaCorrienteService;
		this.patenteRepository = patenteRepository;
	}
	
	public ResponseEntity<List<Estacionamiento>> estacionamientos() {
		return ResponseEntity.ok(this.estacionamientoRepository.findAll());
	}
	
	public ResponseEntity<String> iniciarEstacionamiento(String celular, String cadena) {
		
		if (esFeriado() || esFinSemana()) return ResponseEntity.badRequest().body("El sistema no funciona en feriados, sabados y domingos.");
		
		if (LocalDateTime.now().toLocalTime().isBefore(HORARIO_APERTURA)) return ResponseEntity.badRequest().body("El horario de apertura del sistema de estacionamientos es a las 8:00 am");
		
		if (this.estacionamientoRepository.existsByUsuarioCelularAndFinIsNull(celular)) return ResponseEntity.badRequest().body("Ya existe un estacionamiento iniciado por este usuario.");
		
		CtaCorriente ctaCorriente = this.ctaCorrienteService.buscarCuentaUsuario(celular);
		if (ctaCorriente == null) return ResponseEntity.badRequest().body("Cuenta Corriente no encontrada.");
		
		if (ctaCorriente.getSaldo() < 10.0) return ResponseEntity.badRequest().body("Saldo insuficiente para iniciar un estacionamiento.");
		
		Estacionamiento estacionamiento = new Estacionamiento();
		estacionamiento.setUsuario(ctaCorriente.getUsuario());
		
		Optional<Patente> patente = patenteRepository.findByCadena(cadena);
		
		estacionamiento.setPatente(patente.get());
		
		estacionamientoRepository.save(estacionamiento);
		return ResponseEntity.ok().body("Estacionamiento iniciado!");
		
	}
	
	public ResponseEntity<String> finalizarEstacionamiento(Long id) {
		
		if (esFeriado() || esFinSemana()) return ResponseEntity.badRequest().body("El sistema no funciona en feriados, sabados y domingos.");
		
		LocalDateTime fin = LocalDateTime.now();
		if (fin.toLocalTime().isAfter(HORARIO_CIERRE)) return ResponseEntity.badRequest().body("El horario de cierre del sistema de estacionamientos es a las 20:00 pm");
		
		Optional<Estacionamiento> estacionamientoOpcional = this.estacionamientoRepository.findById(id);
		if (estacionamientoOpcional.isEmpty()) return ResponseEntity.badRequest().body("No se encontro el estacionamiento indicado.");
		
		Estacionamiento estacionamiento = estacionamientoOpcional.get();
		if (estacionamiento.getFin() != null) return ResponseEntity.badRequest().body("El estacionamiento indicado ya finalizó.");
		
		long fracciones = calculoHorasEstacionamiento(estacionamiento.getInicio(), LocalDateTime.now());
		double importeTotal = COSTO_FRACCION * fracciones;
		
		this.ctaCorrienteService.realizarConsumo(estacionamiento.getUsuario().getCelular(), importeTotal);
		
		estacionamiento.setFin(LocalDateTime.now());
		estacionamiento.setImporte(importeTotal);
		estacionamientoRepository.save(estacionamiento);
		
		return ResponseEntity.ok().body("Estacionamiento finalizado!");
		
	}
	
	private long calculoHorasEstacionamiento(LocalDateTime inicio, LocalDateTime fin) {
		// para un sistema que cuenta horas completas se trabajaria dividiendo la cantidad de minutos entre duraciones por 60
		// en cambio, si se aplican cobros con un esquema de fraccionamiento se deberia dividir por la cantidad de minutos que
		// se usa para el fraccionamiento, en el caso de una fraccion de 15 minutos a $2.50 seria duracion.toMinutes()/15
		// luego se podria tomar el valor de duracion.toMinutes() % 15 para que me de los minutos que sobraron y tomarlos con el valor de 15 minutos
		
		double fracciones = 0;
		
		Duration duracion = Duration.between(inicio, fin);
		// para el caso en el que el estacionamiento inicie y termine en el mismo dia
		if (inicio.toLocalDate().equals(fin.toLocalDate())) {
			fracciones = duracion.toMinutes() / ESQUEMA_FRACCIONAMIENTO;
			
		} else {
			// caso en el que inicie en un dia y termine en el siguiente
			long dias = duracion.toDays();
			
			Duration duracionPrimerDia = Duration.between(inicio.toLocalTime(), HORARIO_CIERRE);
			Duration duracionUltimoDia = Duration.between(HORARIO_APERTURA, fin.toLocalTime());
			
			if ( dias <= 1) {
				
				fracciones = (duracionPrimerDia.toMinutes() + duracionUltimoDia.toMinutes() / ESQUEMA_FRACCIONAMIENTO);
			
			} else {
				// caso en el que tome mas de un dia (diferentes dias) ej. empieza un lunes y termina un jueves
				long diasCompletos = (fin.getDayOfMonth() - inicio.getDayOfMonth()) + 1;
				long diasIntermedios = diasCompletos - 2;
				double horasCompletasEnMinutos = ((diasIntermedios * (HORARIO_CIERRE.getHour() - HORARIO_APERTURA.getHour())) * 60) / ESQUEMA_FRACCIONAMIENTO;
				
				fracciones = ((duracionPrimerDia.toMinutes() + duracionUltimoDia.toMinutes()) / ESQUEMA_FRACCIONAMIENTO) + horasCompletasEnMinutos;
			}

		}
		
		if ((fracciones % ESQUEMA_FRACCIONAMIENTO) > 0) fracciones ++;
		return (long) fracciones;
		
	}

	public ResponseEntity<List<Estacionamiento>> estacionamientosUsuario(String celular) {
		return ResponseEntity.ok(this.estacionamientoRepository.findAllByUsuarioCelular(celular));
	}
	
	public ResponseEntity<Optional<Estacionamiento>> estacionamientoPendiente(String celular) {
		return ResponseEntity.ok(this.estacionamientoRepository.findByUsuarioCelularAndFinIsNull(celular));
	}
	
	private boolean esFeriado() {
		LocalDate fechaActual = LocalDate.now();
		int añoActual = fechaActual.getYear();
		return Arrays.stream(Feriado.values()).anyMatch(feriado -> feriado.getFecha(añoActual).equals(fechaActual));
	}
	
	private boolean esFinSemana() {
		LocalDateTime fechaActual = LocalDateTime.now();
		if (fechaActual.getDayOfWeek().equals(DayOfWeek.SATURDAY) || fechaActual.getDayOfWeek().equals(DayOfWeek.SUNDAY)) return true;
		return false;
	}
	
}
