package com.entrenamiento.appbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entrenamiento.appbackend.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String>{

	Optional<Usuario> findByCelular(String celular);

}
