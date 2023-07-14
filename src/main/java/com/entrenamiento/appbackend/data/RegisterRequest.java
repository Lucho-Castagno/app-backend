package com.entrenamiento.appbackend.data;

public class RegisterRequest {
	
	private String celular;
	private String contraseña;
	private String email;
	
	public RegisterRequest() {
		super();
	}
	
	public RegisterRequest(String celular, String contraseña, String email) {
		super();
		this.celular = celular;
		this.contraseña = contraseña;
		this.email = email;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
