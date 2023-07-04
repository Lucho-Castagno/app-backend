package com.entrenamiento.appbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.service.CtaCorrienteService;

@RestController
@RequestMapping("/cuenta")
public class CtaCorrienteController {
	
	private final CtaCorrienteService ctaCorrienteService;
	
	public CtaCorrienteController(CtaCorrienteService ctaCorrienteService) {
		this.ctaCorrienteService = ctaCorrienteService;
	}

	@PostMapping("/{id}/{monto}")
	public ResponseEntity<?> cargarSaldoCta(@PathVariable("id") Long id, @PathVariable double monto) {
		return this.ctaCorrienteService.cargarSaldo(id, monto);
	}
	
}
