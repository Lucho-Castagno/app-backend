package com.entrenamiento.appbackend.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AccountMovements {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDateTime date;
	
	private String type;
	
	@Column(columnDefinition = "DECIMAL(19,2)")
	private double amount;
	
	@ManyToOne
	@JoinColumn(name = "accountId")
	@JsonIgnore
	private CheckingAccount account;

	public AccountMovements() {
		super();
	}

	public AccountMovements(LocalDateTime date, double amount, CheckingAccount account, String type) {
		super();
		this.date = date;
		this.amount = amount;
		this.account = account;
		this.type = type;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public CheckingAccount getAccount() {
		return account;
	}

	public void setAccount(CheckingAccount account) {
		this.account = account;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
