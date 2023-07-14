package com.entrenamiento.appbackend.data;

public class AuthResponse {
	
	private String token;
	private String celular;
	
	public AuthResponse() {
		super();
	}

	public AuthResponse(String token, String celular) {
		super();
		this.token = token;
		this.celular = celular;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}
	
}
