package com.entrenamiento.appbackend.service;

import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.entrenamiento.appbackend.exception.AppRequestException;
import com.entrenamiento.appbackend.model.Plate;
import com.entrenamiento.appbackend.model.Usser;
import com.entrenamiento.appbackend.repository.PlateRepository;
import com.entrenamiento.appbackend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class PlateServiceTest {
	
	@Mock
	private PlateRepository plateRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private PlateService plateService;
	
	private Usser user = new Usser("1122916097", "password", "email@email.com");
	
	@Test
	void testCreate_correct() {
		
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		
		Plate plate = (Plate) plateService.create(1L, "ABC123").getBody();
		
		assertThat(plate.getPlate().equals("ABC123"));
	}
	
	@Test
	void testCreate_badFormat() {
		
		try {
	        plateService.create(1L, "ABC123a");
	    } catch (AppRequestException e) {
	        assertThat(e.getMessage()).isEqualTo("Formato de patente: AAA000 ó AA000AA.");
	    }
		
	}
	
	@Test
	void testCreate_userNotFound() {
		
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		
		try {
	        plateService.create(1L, "ABC123");
	    } catch (AppRequestException e) {
	        assertThat(e.getMessage()).isEqualTo("No se encontro al usuario.");
	    }
		
	}
	
}
