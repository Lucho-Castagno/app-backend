package com.entrenamiento.appbackend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.model.Estacionamiento;
import com.entrenamiento.appbackend.repository.EstacionamientoRepository;

@RestController
@RequestMapping("/api/estacionamientos")
public class EstacionamientoController {
	
	private final EstacionamientoRepository estacionamientoRepository;
	
	public EstacionamientoController(EstacionamientoRepository estacionamientoRepository) {
		this.estacionamientoRepository = estacionamientoRepository;
	}
	
	@GetMapping("")
	public List<Estacionamiento> obtenerUsuarios() {
		return this.estacionamientoRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Optional<Estacionamiento> obtenerUsuario(@PathVariable Long id) {
		return this.estacionamientoRepository.findById(id);
	}
	
	@PostMapping("")
	public Estacionamiento crearUsuario(@RequestBody Estacionamiento usuario) {
		return this.estacionamientoRepository.save(usuario);
	}
	
}
