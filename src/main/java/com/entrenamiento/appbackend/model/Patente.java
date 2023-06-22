package com.entrenamiento.appbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Patente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String cadena;

	public Patente(Long id, String cadena) {
		super();
		this.id = id;
		this.cadena = cadena;
	}

	public Patente() {
		super();
	}

	public Long getId() {
		return id;
	}

	public String getCadena() {
		return cadena;
	}

	public void setCadena(String cadena) {
		this.cadena = cadena;
	}
	
}
