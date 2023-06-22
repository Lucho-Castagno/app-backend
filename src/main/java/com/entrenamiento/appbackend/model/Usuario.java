package com.entrenamiento.appbackend.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Usuario {
	
	@Id
	private String celular;
	
	private String contraseña;
	
	// many to many porque puede que una misma patente (coche) pueda ser usuado por varias personas, y estan tengan diferentes cuentas,
	// como por ejemplo, un auto compartido por una pareja o una camioneta de entregas utilizada por varios trabajadores.
	@ManyToMany
	@JoinTable( 
			name="usuario_patente",
			joinColumns=@JoinColumn(name="idUsuario"),
			inverseJoinColumns=@JoinColumn(name="idPatente")
			)
	private Set<Patente> patentes;
	
	public Usuario() {
		super();
	}

	public Usuario(String celular, String contraseña, Set<Patente> patentes) {
		super();
		this.celular = celular;
		this.contraseña = contraseña;
		this.patentes = patentes;
	}

	public void addPatente(Patente patente) {
		this.patentes.add(patente);
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

	public Set<Patente> getPatentes() {
		return patentes;
	}

	public void setPatentes(Set<Patente> patentes) {
		this.patentes = patentes;
	}
	
}