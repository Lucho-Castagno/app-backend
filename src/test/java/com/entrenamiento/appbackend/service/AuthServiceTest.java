package com.entrenamiento.appbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.entrenamiento.appbackend.data.AuthRequest;
import com.entrenamiento.appbackend.data.AuthResponse;
import com.entrenamiento.appbackend.data.RegisterRequest;
import com.entrenamiento.appbackend.exception.AppRequestException;
import com.entrenamiento.appbackend.model.Usser;
import com.entrenamiento.appbackend.repository.CheckingAccountRepository;
import com.entrenamiento.appbackend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private CheckingAccountRepository checkingAccountRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private JwtService jwtService;
	
	@Mock
	private AuthenticationManager authenticationManager;
	
	@InjectMocks
	private AuthService authService;
	
	@Captor
	ArgumentCaptor<Usser> userCaptor;
	
	@Test
	void testRegister_emptyRequest() {
		
		RegisterRequest request = new RegisterRequest("", "", "");
		
		Exception e = assertThrows(AppRequestException.class, () -> authService.register(request));
		assertThat(e.getMessage()).isEqualTo("Todos los campos son requeridos.");
		
	}
	
	@Test
	void testRegister_cellphoneFormat() {
		
		RegisterRequest request = new RegisterRequest("01122917085", "password", "email@email.com");
		
		Exception e = assertThrows(AppRequestException.class, () -> authService.register(request));
		assertThat(e.getMessage()).isEqualTo("El celular debe tener 10 digitos, sin contar el 0 ni el 15.");
		
	}
	
	@Test
	void testRegister_emailFormat() {
		
		RegisterRequest request = new RegisterRequest("1122917085", "password", "email@email.");
		
		Exception e = assertThrows(AppRequestException.class, () -> authService.register(request));
		assertThat(e.getMessage()).isEqualTo("El formato del email es invalido.");
		
	}
	
	@Test
	void testRegister_cellphoneAlreadyExist() {
		
		Usser user = new Usser("1122917085", "password", "email@email.com");
		
		RegisterRequest request = new RegisterRequest("1122917085", "password", "email@email.com");
		when(userRepository.findByCellphone(request.getCellphone())).thenReturn(Optional.of(user));
		
		Exception e = assertThrows(AppRequestException.class, () -> authService.register(request));
		assertThat(e.getMessage()).isEqualTo("El celular que intenta ingresar ya esta registrado en el sistema.");
		
	}
	
	@Test
	void testRegister_correct() {
		
		RegisterRequest request = new RegisterRequest("1122917085", "password", "email@email.com");
		when(userRepository.findByCellphone(request.getCellphone())).thenReturn(Optional.empty());
		
		ResponseEntity<String> response = authService.register(request);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo("Usuario registrado con exito!");
		
		verify(userRepository).save(userCaptor.capture());
		Usser registeredUser = userCaptor.getValue();
		
		assertEquals(request.getCellphone(), registeredUser.getCellphone());
		assertEquals(request.getEmail(), registeredUser.getEmail());
		assertNotNull(registeredUser.getAccount());
		
	}
	
	@Test
	void testLogin_emptyRequest() {
		
		AuthRequest request = new AuthRequest("", "");
		
		Exception e = assertThrows(AppRequestException.class, () -> authService.login(request));
		assertThat(e.getMessage()).isEqualTo("Todos los campos son requeridos.");
		
	}
	
	@Test
	void testLogin_userNotFound() {
		
		AuthRequest request = new AuthRequest("1122917085", "password");
		when(userRepository.findByCellphone(request.getCellphone())).thenReturn(Optional.empty());
		
		Exception e = assertThrows(AppRequestException.class, () -> authService.login(request));
		assertThat(e.getMessage()).isEqualTo("El usuario no esta registrado en el sistema.");
		
	}
	
	@Test
	void testLogin_correct() {
		
		Usser user = new Usser("1122917085", "password", "email@email.com");
		user.setId(1L);
		when(jwtService.generateToken(user)).thenReturn("aS2csafeEARfsawc");
		
		AuthRequest request = new AuthRequest("1122917085", "password");
		when(userRepository.findByCellphone(request.getCellphone())).thenReturn(Optional.of(user));
		
		ResponseEntity<?> response = authService.login(request);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		AuthResponse authResponse = ((AuthResponse) response.getBody());
		
		assertEquals(user.getId(), authResponse.getId());
		assertNotNull(authResponse.getToken());
		
	}
	
}
