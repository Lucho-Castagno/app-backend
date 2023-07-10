package com.entrenamiento.appbackend.exception;

public class AppRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public AppRequestException(String mensaje) {
		super(mensaje);
	}

}
