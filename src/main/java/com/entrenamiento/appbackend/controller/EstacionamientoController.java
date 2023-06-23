package com.entrenamiento.appbackend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.model.Estacionamiento;
import com.entrenamiento.appbackend.model.Patente;
import com.entrenamiento.appbackend.service.EstacionamientoService;


@RestController
@RequestMapping("/api/estacionamientos")
public class EstacionamientoController {
	
	private final EstacionamientoService estacionamientoService;
	
	public EstacionamientoController(EstacionamientoService estacionamientoService) {
		this.estacionamientoService = estacionamientoService;
	}
	
	@GetMapping("")
	public List<Estacionamiento> obtenerEstacionamientos() {
		return this.estacionamientoService.estacionamientos();
	}
	
	@GetMapping("/{id}")
	public Optional<Estacionamiento> obtenerEstacionamiento(@PathVariable Long id) {
		return this.estacionamientoService.estacionamientoPorId(id);
	}
	
	@PostMapping("/iniciar/{celular}")
	public ResponseEntity<String> iniciarEstacionamiento(@PathVariable("celular") String celular, @RequestBody Patente patente) {
		try {
			this.estacionamientoService.iniciarEstacionamiento(celular, patente);
			return ResponseEntity.ok("El estacionamiento inició con exito!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Inicio de estacionamiento fallido: " + e.getMessage());
		}
	}
	
	@PostMapping("/{id}/finalizar")
	public ResponseEntity<String> finalizarEstacionamiento(@PathVariable Long id) {
		try {
			this.estacionamientoService.finalizarEstacionamiento(id);
			return ResponseEntity.ok("El estacionamiento finalizó con exito!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El estacionamiento no pudo finalizar: " + e.getMessage());
		}
	}
	
}
