package com.entrenamiento.appbackend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.model.Patente;
import com.entrenamiento.appbackend.model.Usuario;
import com.entrenamiento.appbackend.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
	private final UsuarioService usuarioService;
	
	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@GetMapping("")
	public List<Usuario> obtenerUsuarios() {
		return usuarioService.usuarios();
	}
	
	@GetMapping("/{celular}/patentes")
	public List<Patente> obtenerPatentesUsuario(@PathVariable("celular") String celular) {
		return usuarioService.obtenerPatentesUsuario(celular);
	}
	
	@GetMapping("/{celular}")
	public Optional<Usuario> obtenerUsuario(@PathVariable String celular) {
		return usuarioService.usuarioPorId(celular);
	}
	
	@PostMapping("")
	public Usuario crearUsuario(@RequestBody Usuario usuario) {
		return this.usuarioService.crearUsuario(usuario);
	}
	
}
