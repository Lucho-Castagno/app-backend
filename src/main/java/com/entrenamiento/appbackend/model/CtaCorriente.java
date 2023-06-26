package com.entrenamiento.appbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class CtaCorriente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	private double saldo = 10000.0;
	
	@OneToOne(mappedBy = "ctaCorriente")
	private Usuario usuario;

	public CtaCorriente() {
		super();
	}

	public CtaCorriente(Usuario usuario) {
		super();
		this.usuario = usuario;
	}

	public Long getId() {
		return id;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
