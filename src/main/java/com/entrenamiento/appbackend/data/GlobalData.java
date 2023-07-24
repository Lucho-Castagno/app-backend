package com.entrenamiento.appbackend.data;

import java.time.LocalTime;

public class GlobalData {
	
	private final LocalTime OPENING_HOUR = LocalTime.of(8, 0);
	private final LocalTime CLOSING_HOUR = LocalTime.of(20, 0);
	private final Double FRACTIONATION_SCHEME = 15.0;
	private final Double FRACTION_COST = 2.50;
	
	public LocalTime getOpeningHour() {
		return OPENING_HOUR;
	}
	public LocalTime getClosingHour() {
		return CLOSING_HOUR;
	}
	public Double getFractionationScheme() {
		return FRACTIONATION_SCHEME;
	}
	public Double getFractionCost() {
		return FRACTION_COST;
	}
	
}
