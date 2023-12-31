package com.entrenamiento.appbackend;

import java.time.LocalDate;
import java.time.Month;

public enum Holiday {
	AÑO_NUEVO(Month.JANUARY, 1),
	DIA_CARNAVAL_1(Month.FEBRUARY, 20),
	DIA_CARNAVAL_2(Month.FEBRUARY, 21),
	DIA_MEMORIA(Month.MARCH, 24),
	DIA_MALVINAS(Month.APRIL, 2),
	JUEVES_SANTO(Month.APRIL, 6),
	VIERNES_SANTO(Month.APRIL, 7),
	DIA_TRABAJO(Month.MAY, 1),
	DIA_REVOLUCION(Month.MAY, 25),
	DIA_GUEMES(Month.JUNE, 17),
	DIA_BELGRANO(Month.JUNE, 20),
	DIA_INDEPENDENCIA(Month.JULY, 9),
	DIA_SAN_MARTIN(Month.AUGUST, 17),
	DIA_DIVERSIDAD(Month.OCTOBER, 12),
	DIA_SOBERANIA(Month.NOVEMBER, 20),
	INMACULADA_CONCEPCION(Month.DECEMBER, 8),
	NAVIDAD(Month.DECEMBER, 25);
	
	private final Month month;
	private final int day;
	
	Holiday(Month month, int day) {
		this.month = month;
		this.day = day;
	}
	
	public LocalDate getDate(int year) {
		return LocalDate.of(year, month, day);
	}
}
