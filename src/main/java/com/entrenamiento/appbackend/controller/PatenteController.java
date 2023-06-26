package com.entrenamiento.appbackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.model.Patente;
import com.entrenamiento.appbackend.model.Usuario;
import com.entrenamiento.appbackend.service.PatenteService;

@RestController
@RequestMapping("/api/patentes")
public class PatenteController {
	
	private final PatenteService patenteService;
	
	public PatenteController(PatenteService patenteService) {
		this.patenteService = patenteService;
	}
	
	@GetMapping("")
	public List<Patente> obtenerPatentes() {
		return patenteService.patentes();
	}
	
	@PostMapping("/{cadena}")
	public Patente crearPatente(@PathVariable("cadena") String cadena, @RequestBody Usuario usuario) {
		return this.patenteService.crearPatente(cadena, usuario);
	}
	
}
