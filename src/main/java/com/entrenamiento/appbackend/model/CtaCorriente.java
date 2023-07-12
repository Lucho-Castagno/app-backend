package com.entrenamiento.appbackend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class CtaCorriente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column(columnDefinition = "DECIMAL(19,2)")
	private Double saldo = 0.0;
	
	@OneToOne(mappedBy = "ctaCorriente")
	@JsonIgnore
	private Usuario usuario;
	
	@OneToMany(mappedBy = "cuentaCorriente")
	@JsonIgnore
	private List<MovimientoCta> movimientosCta = new ArrayList<>();

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

	public void consumo(double importeTotal) {
		this.saldo = this.saldo - importeTotal;
	}
	
	public void cargarCredito(double credito) {
		this.saldo = this.saldo + credito;
	}
	
	public void addMovimiento(MovimientoCta movimiento) {
		movimientosCta.add(movimiento);
	}

	public List<MovimientoCta> getMovimientosCta() {
		return movimientosCta;
	}
	
}
