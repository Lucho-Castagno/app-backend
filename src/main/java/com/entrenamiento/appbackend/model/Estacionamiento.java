package com.entrenamiento.appbackend.model;

import java.sql.Date;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Estacionamiento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@CreatedDate
	private Date inicio;
	
	private Date fin;
	
	private double importe;
	
	@OneToOne
	@JoinColumn(name="id_patente")
	private Patente patente;
	
	@OneToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
	
}
