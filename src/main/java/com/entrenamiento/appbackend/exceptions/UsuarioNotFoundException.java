package com.entrenamiento.appbackend.exceptions;

public class UsuarioNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsuarioNotFoundException(String mensaje) {
		super(mensaje);
	}

}
