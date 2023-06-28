package com.entrenamiento.appbackend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.model.Estacionamiento;
import com.entrenamiento.appbackend.service.EstacionamientoService;


@RestController
@RequestMapping("/estacionamientos")
@CrossOrigin
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
	public ResponseEntity<String> iniciarEstacionamiento(@PathVariable("celular") String celular, @RequestParam("patente") String patente) {
		
		return this.estacionamientoService.iniciarEstacionamiento(celular, patente);
			
	}
	
	@PostMapping("/{id}/finalizar")
	public ResponseEntity<String> finalizarEstacionamiento(@PathVariable Long id) {
		
		return this.estacionamientoService.finalizarEstacionamiento(id);
			
	}
	
}
