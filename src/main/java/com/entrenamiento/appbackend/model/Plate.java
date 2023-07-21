package com.entrenamiento.appbackend.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Plate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String plate;
	
	@ManyToMany(mappedBy = "plates")
    private Set<Usser> users = new HashSet<>();
	
	@OneToMany(mappedBy = "plate")
	private List<Parking> parkings;

	public Plate(String plate) {
		super();
		this.plate = plate;
	}

	public Plate() {
		super();
	}

	public Long getId() {
		return id;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}
	
	public void addUser(Usser usser) {
		this.users.add(usser);
	}
	
}
