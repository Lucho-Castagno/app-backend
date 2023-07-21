package com.entrenamiento.appbackend.data;

public class AuthResponse {
	
	private String token;
	private Long userId;
	
	public AuthResponse() {
		super();
	}

	public AuthResponse(String token, Long id) {
		super();
		this.token = token;
		this.userId = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public Long getId() {
		return userId;
	}

	public void setId(Long id) {
		this.userId = id;
	}
	
}
