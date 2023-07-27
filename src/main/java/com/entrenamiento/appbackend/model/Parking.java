package com.entrenamiento.appbackend.model;

import java.time.LocalDateTime;

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
public class Parking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDateTime start;
	
	private LocalDateTime endParking;
	
	private double amount;
	
	@ManyToOne
	@JoinColumn(name = "plateId")
	private Plate plate;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	@JsonIgnore
	private Usser user;

	public Parking() {
		super();
		this.start = LocalDateTime.now();
	}

	public Parking(Long id, LocalDateTime start, LocalDateTime end, double amount, Plate plate, Usser usser) {
		super();
		this.id = id;
		this.start = start;
		this.endParking = end;
		this.amount = amount;
		this.plate = plate;
		this.user = usser;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return endParking;
	}

	public void setEnd(LocalDateTime end) {
		this.endParking = end;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Plate getPlate() {
		return plate;
	}

	public void setPlate(Plate plate) {
		this.plate = plate;
	}

	public Usser getUser() {
		return user;
	}

	public void setUser(Usser usser) {
		this.user = usser;
	}
	
}
