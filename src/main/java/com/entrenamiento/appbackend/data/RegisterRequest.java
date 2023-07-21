package com.entrenamiento.appbackend.data;

public class RegisterRequest {
	
	private String cellphone;
	private String password;
	private String email;
	
	public RegisterRequest() {
		super();
	}
	
	public RegisterRequest(String cellphone, String password, String email) {
		super();
		this.cellphone = cellphone;
		this.password = password;
		this.email = email;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
