package com.entrenamiento.appbackend;

public enum TipoMovimiento {
	CONSUMO("CONSUMO"),
	CARGA("CARGA");
	
	private final String descripcion;
	
	TipoMovimiento(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getDescripcion() {
		return this.descripcion;
	}
}
