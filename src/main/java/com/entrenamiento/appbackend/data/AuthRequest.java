package com.entrenamiento.appbackend.data;

public class AuthRequest {

	private String cellphone;
	private String password;
	
	public AuthRequest() {
		super();
	}
	
	public AuthRequest(String cellphone, String password) {
		super();
		this.cellphone = cellphone;
		this.password = password;
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
	
}
