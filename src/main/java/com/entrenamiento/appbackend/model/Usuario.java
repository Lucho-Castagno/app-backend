package com.entrenamiento.appbackend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Usuario {
	
	@Id
	private String celular;
	
	private String contraseña;
	
	// many to many porque puede que una misma patente (coche) pueda ser usuado por varias personas, y estan tengan diferentes cuentas,
	// como por ejemplo, un auto compartido por una pareja o una camioneta de entregas utilizada por varios trabajadores.
	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable( 
			name = "usuario_patente",
			joinColumns = @JoinColumn(name = "celular"),
			inverseJoinColumns = @JoinColumn(name = "idPatente")
	)
	private List<Patente> patentes = new ArrayList<>();	
	
	@OneToOne
	@JoinColumn(name = "ctaCorriente")
	private CtaCorriente ctaCorriente;
	
	public Usuario() {
		super();
	}

	public Usuario(String celular, String contraseña) {
		super();
		this.celular = celular;
		this.contraseña = contraseña;
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

	public List<Patente> getPatentes() {
		return patentes;
	}

	public void setPatentes(List<Patente> patentes) {
		this.patentes = patentes;
	}

	public void setCtaCorriente(CtaCorriente ctaCorriente) {
		this.ctaCorriente = ctaCorriente;
	}
	
	public CtaCorriente getCtaCorriente() {
		return this.ctaCorriente;
	}
	
}