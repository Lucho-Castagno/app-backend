package com.entrenamiento.appbackend.model;

import java.time.LocalDateTime;

import com.entrenamiento.appbackend.TipoMovimiento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class MovimientoCta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDateTime fecha;
	
	@Enumerated(EnumType.STRING)
	private TipoMovimiento tipo;
	
	@Column(columnDefinition = "DECIMAL(19,2)")
	private double monto;
	
	@ManyToOne
	@JoinColumn(name = "cta_id")
	private CtaCorriente cuentaCorriente;

	public MovimientoCta() {
		super();
	}

	public MovimientoCta(LocalDateTime fecha, double monto, CtaCorriente cuentaCorriente, TipoMovimiento tipo) {
		super();
		this.fecha = fecha;
		this.monto = monto;
		this.cuentaCorriente = cuentaCorriente;
		this.tipo = tipo;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public CtaCorriente getCuentaCorriente() {
		return cuentaCorriente;
	}

	public void setCuentaCorriente(CtaCorriente cuentaCorriente) {
		this.cuentaCorriente = cuentaCorriente;
	}

	public TipoMovimiento getTipo() {
		return tipo;
	}

	public void setTipo(TipoMovimiento tipo) {
		this.tipo = tipo;
	}
	
}
