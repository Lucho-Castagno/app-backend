package com.entrenamiento.appbackend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.entrenamiento.appbackend.service.ParkingService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ParkingControllerTest {
	
	private ParkingService parkingService;
	
	private MockMvc mockMvc;
	
	private Long userId = 1L;
	private String plate = "ABC123";
	private Long parkingId = 1L;
	
	@BeforeEach
	public void setUp() {
		parkingService = mock(ParkingService.class);
		mockMvc = MockMvcBuilders.standaloneSetup(new ParkingController(parkingService)).build();
	}
	
	@Test
	public void testStartParking() throws Exception {
        when(parkingService.startParking(userId, plate)).thenReturn(new ResponseEntity<>("Estacionamiento iniciado!", HttpStatus.OK));

        MvcResult result = mockMvc.perform(post("/parkings/start/{userId}", userId)
                .param("plate", plate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Estacionamiento iniciado!"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("Estacionamiento iniciado!", content);

        verify(parkingService, times(1)).startParking(userId, plate);
        System.out.println("Status de la respuesta: " + result.getResponse().getStatus());
        System.out.println("Contenido de la respuesta: " + result.getResponse().getContentAsString());
        System.out.println("Encabezados de la respuesta: " + result.getResponse().getHeaderNames());
	}
	
	@Test
	public void testFinishParking() throws Exception {
		when(parkingService.finishParking(parkingId)).thenReturn(new ResponseEntity<>("Estacionamiento finalizado!", HttpStatus.OK));

        MvcResult result = mockMvc.perform(post("/parkings/{parkingId}/finish", parkingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Estacionamiento finalizado!"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("Estacionamiento finalizado!", content);

        verify(parkingService, times(1)).finishParking(parkingId);
        System.out.println("Status de la respuesta: " + result.getResponse().getStatus());
        System.out.println("Contenido de la respuesta: " + result.getResponse().getContentAsString());
        System.out.println("Encabezados de la respuesta: " + result.getResponse().getHeaderNames());	
	}
	
}
