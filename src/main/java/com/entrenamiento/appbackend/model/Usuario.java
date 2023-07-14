package com.entrenamiento.appbackend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Usuario implements UserDetails{
	
	@Id
	private String celular;
	
	private String contraseña;
	
	private String email;
	
	// many to many porque puede que una misma patente (coche) pueda ser usuado por varias personas, y estan tengan diferentes cuentas,
	// como por ejemplo, un auto compartido por una pareja o una camioneta de entregas utilizada por varios trabajadores.
	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable( 
			name = "usuario_patente",
			joinColumns = @JoinColumn(name = "celular"),
			inverseJoinColumns = @JoinColumn(name = "idPatente")
	)
	@JsonIgnore
	private List<Patente> patentes = new ArrayList<>();	
	
	@OneToOne
	@JoinColumn(name = "ctaCorriente")
	@JsonIgnore
	private CtaCorriente ctaCorriente;
	
	@OneToMany(mappedBy = "usuario")
	@JsonIgnore
	private List<Estacionamiento> estacionamientos;
	
	public Usuario() {
		super();
	}

	public Usuario(String celular, String contraseña, String email) {
		super();
		this.celular = celular;
		this.contraseña = contraseña;
		this.email = email;
	}

	public void addPatente(Patente patente) {
		this.patentes.add(patente);
	}
	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	//public String getContraseña() {
	//	return contraseña;
	//}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public List<Patente> getPatentes() {
		return patentes;
	}

	public void setPatentes(List<Patente> patentes) {
		this.patentes = patentes;
	}

	public void setCtaCorriente(CtaCorriente ctaCorriente) {
		this.ctaCorriente = ctaCorriente;
	}
	
	public CtaCorriente getCtaCorriente() {
		return this.ctaCorriente;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return contraseña;
	}

	@Override
	public String getUsername() {
		return celular;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}