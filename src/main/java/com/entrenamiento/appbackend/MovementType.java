package com.entrenamiento.appbackend;

public enum MovementType {
	TRANSACTION("CONSUMO"),
	CHARGE("CARGA");
	
	private final String description;
	
	MovementType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
}
