package com.ciberfarma.repository;

import com.ciberfarma.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	   Optional<Usuario> findByUsuarioAndClave(String usuario, String clave);

	    boolean existsByUsuario(String usuario);
}