package com.entrenamiento.appbackend.data;

public class AuthRequest {

	private String celular;
	private String contraseña;
	
	public AuthRequest() {
		super();
	}
	
	public AuthRequest(String celular, String contraseña) {
		super();
		this.celular = celular;
		this.contraseña = contraseña;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getContraseña() {
		return contraseña;
	}
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	
}
