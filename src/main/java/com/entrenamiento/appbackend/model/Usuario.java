package com.entrenamiento.appbackend.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario {
	
	@Id
	private String celular;
	
	private String contraseña;
	
	// many to many porque puede que una misma patente (coche) pueda ser usuado por varias personas, y estan tengan diferentes cuentas,
	// como por ejemplo, un auto compartido por una pareja o una camioneta de entregas utilizada por varios trabajadores.
	@ManyToMany
	@JoinTable( 
			name="usuario_patente",
			joinColumns=@JoinColumn(name="id_usuario"),
			inverseJoinColumns=@JoinColumn(name="id_patente")
			)
	private Set<Patente> patentes;
	
	@OneToOne
	@JoinColumn(name="id_cta")
	private CtaCorriente cuenta;
	
	public void addPatente(Patente patente) {
		this.patentes.add(patente);
	}
	
}