package com.entrenamiento.appbackend.exceptions;

public class CtaCorrienteNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CtaCorrienteNotFoundException(String mensaje) {
		super(mensaje);
	}

}
