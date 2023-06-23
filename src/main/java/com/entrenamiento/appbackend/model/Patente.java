package com.entrenamiento.appbackend.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Patente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String cadena;
	
	@ManyToMany(mappedBy = "patentes")
    private Set<Usuario> usuarios = new HashSet<>();

	public Patente(String cadena) {
		super();
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
	
	public void addUsuario(Usuario usuario) {
		this.usuarios.add(usuario);
	}
	
}
