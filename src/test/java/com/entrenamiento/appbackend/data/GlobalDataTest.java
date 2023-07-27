package com.entrenamiento.appbackend.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

// esta clase la cree para probar si cambiaban los reportes de jacoco, tomando esta clase como prueba ya que es simple de testear.

public class GlobalDataTest {
	
	private GlobalData globalData = new GlobalData();
	
	@Test
	void testGetOpeningHour() {
		assertNotNull(globalData.getOpeningHour());
		assertEquals(globalData.getOpeningHour(), LocalTime.of(8, 0));
	}
	
	@Test
	void testGetClosingHour() {
		assertNotNull(globalData.getClosingHour());
		assertEquals(globalData.getClosingHour(), LocalTime.of(20, 0));
	}
	
	@Test
	void testGetFractionCost() {
		assertNotNull(globalData.getFractionCost());
		assertEquals(globalData.getFractionCost(), 2.50);
	}
	
	@Test
	void testGetFractionationScheme() {
		assertNotNull(globalData.getFractionationScheme());
		assertEquals(globalData.getFractionationScheme(), 15.0);
	}
	
}
