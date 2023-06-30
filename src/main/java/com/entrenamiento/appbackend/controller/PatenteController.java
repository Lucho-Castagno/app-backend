package com.entrenamiento.appbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.model.Patente;
import com.entrenamiento.appbackend.service.PatenteService;

@RestController
@RequestMapping("/patentes")
public class PatenteController {
	
	private final PatenteService patenteService;
	
	public PatenteController(PatenteService patenteService) {
		this.patenteService = patenteService;
	}
	
	@GetMapping("")
	public List<Patente> obtenerPatentes() {
		return patenteService.patentes();
	}
	
	@PostMapping("/{celular}/crearPatente")
	public ResponseEntity<Patente> crearPatente(@PathVariable("celular") String celular, @RequestParam("cadena") String cadena) {
		return this.patenteService.crearPatente(celular, cadena);
	}
	
}
