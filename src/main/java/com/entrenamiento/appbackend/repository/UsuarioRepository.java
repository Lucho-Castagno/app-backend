package com.entrenamiento.appbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entrenamiento.appbackend.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String>{

	Optional<Usuario> findByCelular(String celular);

}
