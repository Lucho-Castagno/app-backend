package com.entrenamiento.appbackend.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.entrenamiento.appbackend.data.AuthRequest;
import com.entrenamiento.appbackend.data.AuthResponse;
import com.entrenamiento.appbackend.data.RegisterRequest;
import com.entrenamiento.appbackend.exception.AppRequestException;
import com.entrenamiento.appbackend.model.CtaCorriente;
import com.entrenamiento.appbackend.model.Usuario;
import com.entrenamiento.appbackend.repository.CtaCorrienteRepository;
import com.entrenamiento.appbackend.repository.UsuarioRepository;

@Service
public class AuthService {
	
	private final UsuarioRepository usuarioRepository;
	private final CtaCorrienteRepository ctaCorrienteRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	
	public AuthService(UsuarioRepository usuarioRepository,
			PasswordEncoder passwordEncoder,
			JwtService jwtService,
			CtaCorrienteRepository ctaCorrienteRepository,
			AuthenticationManager authenticationManager) {
		this.usuarioRepository = usuarioRepository;
		this.ctaCorrienteRepository = ctaCorrienteRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}
	
	public ResponseEntity<String> registrarse(RegisterRequest request) {
		
		Optional<Usuario> usuarioExiste = this.usuarioRepository.findByCelular(request.getCelular()); 
		if (usuarioExiste.isPresent()) {
			throw new AppRequestException("El celular que intenta ingresar ya esta registrado en el sistema.");
		}

		if (!validarCelular(request.getCelular())) {
			throw new AppRequestException("El celular debe tener 10 digitos, sin contar el 0 ni el 15.");
		}

		if (!validarMail(request.getEmail())) {
			throw new AppRequestException("El formato del email es invalido.");
		}

		if (request.getCelular().isEmpty() || request.getContraseña().isEmpty() || request.getEmail().isEmpty()) {
			throw new AppRequestException("Todos los campos son requeridos");
		}
		
		CtaCorriente ctaCorriente = new CtaCorriente();
		this.ctaCorrienteRepository.save(ctaCorriente);
		
		Usuario usuario = new Usuario(
				request.getCelular(),
				passwordEncoder.encode(request.getContraseña()),
				request.getEmail()
				);
		
		usuario.setCtaCorriente(ctaCorriente);
		this.usuarioRepository.save(usuario);
		
		return ResponseEntity.ok().body("Usuario registrado con exito!");
	}
	
	public ResponseEntity<?> iniciarSesion(AuthRequest request) {
		
		if (request.getCelular().isEmpty() || request.getContraseña().isEmpty()) {
			throw new AppRequestException("Todos los campos son requeridos.");
		}
		
		this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getCelular(),
						request.getContraseña()
						)
				); 
		Usuario usuario = usuarioRepository.findByCelular(request.getCelular()).orElseThrow(() -> new AppRequestException("El usuario no esta registrado en el sistema."));
		
		var jwtToken = jwtService.generateToken(usuario);
		return ResponseEntity.ok(new AuthResponse(jwtToken, usuario.getCelular()));
	}
	
	private boolean validarCelular(String celular) {
		String patron = "^(?!0|15)[0-9]{2}[0-9]{8}$";
		return celular.matches(patron);
	}
	
	private boolean validarMail(String mail) {
		String patron = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		return mail.matches(patron);
	}
	
}