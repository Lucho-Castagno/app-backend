package com.entrenamiento.appbackend.exceptions;

public class EstacionamientoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EstacionamientoNotFoundException(String mensaje) {
		super(mensaje);
	}

}
