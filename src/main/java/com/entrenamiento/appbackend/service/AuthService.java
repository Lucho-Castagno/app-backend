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
import com.entrenamiento.appbackend.model.CheckingAccount;
import com.entrenamiento.appbackend.model.Usser;
import com.entrenamiento.appbackend.repository.CheckingAccountRepository;
import com.entrenamiento.appbackend.repository.UserRepository;

@Service
public class AuthService {
	
	private final UserRepository userRepository;
	private final CheckingAccountRepository checkingAccountRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	
	public AuthService(UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			JwtService jwtService,
			CheckingAccountRepository checkingAccountRepository,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.checkingAccountRepository = checkingAccountRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}
	
	public ResponseEntity<String> register(RegisterRequest request) {
		
		if (request.getCellphone().isEmpty() && request.getPassword().isEmpty() && request.getEmail().isEmpty()) {
			throw new AppRequestException("Todos los campos son requeridos.");
		}
		
		if (!cellphoneValidation(request.getCellphone())) {
			throw new AppRequestException("El celular debe tener 10 digitos, sin contar el 0 ni el 15.");
		}

		if (!emailValidation(request.getEmail())) {
			throw new AppRequestException("El formato del email es invalido.");
		}
		
		Optional<Usser> presentUser = this.userRepository.findByCellphone(request.getCellphone()); 
		if (presentUser.isPresent()) {
			throw new AppRequestException("El celular que intenta ingresar ya esta registrado en el sistema.");
		}

		CheckingAccount checkingAccount = new CheckingAccount();
		this.checkingAccountRepository.save(checkingAccount);
		
		Usser usser = new Usser(
				request.getCellphone(),
				passwordEncoder.encode(request.getPassword()),
				request.getEmail()
				);
		
		usser.setAccount(checkingAccount);
		this.userRepository.save(usser);
		
		return ResponseEntity.ok().body("Usuario registrado con exito!");
	}
	
	public ResponseEntity<?> login(AuthRequest request) {
		
		if (request.getCellphone().isEmpty() && request.getPassword().isEmpty()) {
			throw new AppRequestException("Todos los campos son requeridos.");
		}
		
		this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getCellphone(),
						request.getPassword()
						)
				); 
		Usser usser = userRepository.findByCellphone(request.getCellphone()).orElseThrow(() -> new AppRequestException("El usuario no esta registrado en el sistema."));
		
		var jwtToken = jwtService.generateToken(usser);
		return ResponseEntity.ok(new AuthResponse(jwtToken, usser.getId()));
	}
	
	private boolean cellphoneValidation(String cellphone) {
		String pattern = "^(?!0|15)[0-9]{2}[0-9]{8}$";
		return cellphone.matches(pattern);
	}
	
	private boolean emailValidation(String email) {
		String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		return email.matches(pattern);
	}
	
}