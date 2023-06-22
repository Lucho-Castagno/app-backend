package com.entrenamiento.appbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class CtaCorriente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private Long id;
	
	private double saldo = 10000.0;
	
	@OneToOne
	@JoinColumn(name="celular")
	private Usuario usuario;

	public CtaCorriente() {
		super();
	}

	public CtaCorriente(Long id, double saldo, Usuario usuario) {
		super();
		this.id = id;
		this.saldo = saldo;
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
