package com.entrenamiento.appbackend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrenamiento.appbackend.model.Usuario;
import com.entrenamiento.appbackend.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
	private final UsuarioRepository usuarioRepository;
	
	public UsuarioController(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	@GetMapping("")
	public List<Usuario> obtenerUsuarios() {
		return usuarioRepository.findAll();
	}
	
	@GetMapping("/{celular}")
	public Optional<Usuario> obtenerUsuario(@PathVariable String celular) {
		return usuarioRepository.findById(celular);
	}
	
	@PostMapping("")
	public Usuario crearUsuario(@RequestBody Usuario usuario) {
		return this.usuarioRepository.save(usuario);
	}
	
}
