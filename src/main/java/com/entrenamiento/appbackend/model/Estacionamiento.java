package com.entrenamiento.appbackend.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Estacionamiento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreatedDate
	private LocalDateTime inicio;
	
	private LocalDateTime fin;
	
	private double importe;
	
	@ManyToOne
	@JoinColumn(name="id_patente")
	private Patente patente;
	
	@ManyToOne
	@JoinColumn(name="celular")
	@JsonIgnore
	private Usuario usuario;

	public Estacionamiento() {
		super();
	}

	public Estacionamiento(Long id, LocalDateTime inicio, LocalDateTime fin, double importe, Patente patente, Usuario usuario) {
		super();
		this.id = id;
		this.inicio = inicio;
		this.fin = fin;
		this.importe = importe;
		this.patente = patente;
		this.usuario = usuario;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getInicio() {
		return inicio;
	}

	public void setInicio(LocalDateTime inicio) {
		this.inicio = inicio;
	}

	public LocalDateTime getFin() {
		return fin;
	}

	public void setFin(LocalDateTime fin) {
		this.fin = fin;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public Patente getPatente() {
		return patente;
	}

	public void setPatente(Patente patente) {
		this.patente = patente;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
