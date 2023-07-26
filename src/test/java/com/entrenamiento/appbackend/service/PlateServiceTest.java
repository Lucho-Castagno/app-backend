package com.entrenamiento.appbackend.service;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		
		Exception e = assertThrows(AppRequestException.class, () ->  plateService.create(1L, "ABC123a"));
		assertThat(e.getMessage()).isEqualTo("Formato de patente: AAA000 ó AA000AA.");
		
	}
	
	@Test
	void testCreate_userNotFound() {
		
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		
		Exception e = assertThrows(AppRequestException.class, () ->  plateService.create(1L, "ABC123"));
		assertThat(e.getMessage()).isEqualTo("No se encontro al usuario.");
		
	}
	
	@Test
	void testCreate_userAlreadyHasThatPlate() {
		
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		
		plateService.create(1L, "ABC123");
		
		Exception e = assertThrows(AppRequestException.class, () ->  plateService.create(1L, "aBc123"));
		assertThat(e.getMessage()).isEqualTo("La patente ya esta asociada a este usuario.");
		
	}
	
	@Test
	void testPlates_notEmpty() {
		
		Plate plateOne = new Plate("ABC123");
		Plate plateTwo = new Plate("CBA321");
		
		when(plateRepository.findAll()).thenReturn(List.of(plateOne, plateTwo));
		
		List<Plate> plateList = plateService.plates().getBody();
		
		assertThat(!plateList.isEmpty());
		assertThat(plateList.size()).isEqualTo(2);
		
	}
	
	@Test
	void testPlates_empty() {
		
		when(plateRepository.findAll()).thenReturn(List.of());
		
		List<Plate> plateList = plateService.plates().getBody();
		
		assertTrue(plateList.isEmpty());
		
	}
	
}
