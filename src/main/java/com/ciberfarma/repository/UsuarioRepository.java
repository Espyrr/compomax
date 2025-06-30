package com.ciberfarma.repository;

import com.ciberfarma.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	Optional<Usuario> findByCorreoAndClave(String correo, String clave);
	boolean existsByCorreo(String correo);

}