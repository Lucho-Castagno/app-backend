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
public class CheckingAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "DECIMAL(19,2)")
	private Double balance = 10000.00;
	
	@OneToOne(mappedBy = "account")
	@JsonIgnore
	private Usser user;
	
	@OneToMany(mappedBy = "account")
	@JsonIgnore
	private List<AccountMovements> movements = new ArrayList<>();

	public CheckingAccount() {
		super();
	}

	public CheckingAccount(Usser usser) {
		super();
		this.user = usser;
	}

	public Long getId() {
		return id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Usser getUser() {
		return user;
	}

	public void setUser(Usser usser) {
		this.user = usser;
	}

	public void transaction(double totalAmount) {
		this.balance = this.balance - totalAmount;
	}
	
	public void charge(double credit) {
		this.balance = this.balance + credit;
	}
	
	public void addMovements(AccountMovements movement) {
		this.movements.add(movement);
	}

	public List<AccountMovements> getAccountMovements() {
		return movements;
	}
	
}
