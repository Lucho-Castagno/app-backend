package com.entrenamiento.appbackend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.model.CtaCorriente;
import com.entrenamiento.appbackend.model.Patente;
import com.entrenamiento.appbackend.model.Usuario;
import com.entrenamiento.appbackend.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	private final UsuarioService usuarioService;
	
	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@GetMapping("")
	public ResponseEntity<List<Usuario>> obtenerUsuarios() {
		return usuarioService.usuarios();
	}
	
	@GetMapping("/{celular}/patentes")
	public ResponseEntity<List<Patente>> obtenerPatentesUsuario(@PathVariable("celular") String celular) {
		return usuarioService.obtenerPatentesUsuario(celular);
	}
	
	@GetMapping("/{celular}")
	public ResponseEntity<Optional<Usuario>> obtenerUsuario(@PathVariable String celular) {
		return usuarioService.usuarioPorId(celular);
	}
	
	@GetMapping("/{celular}/cuenta")
	public ResponseEntity<CtaCorriente> obtenerCuentaUsuario(@PathVariable("celular") String celular) {
		return this.usuarioService.obtenerCuentaUsuario(celular);
	}
	
}
