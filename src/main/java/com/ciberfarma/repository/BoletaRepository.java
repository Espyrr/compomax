package com.ciberfarma.repository;

import com.ciberfarma.model.Boleta;
import com.ciberfarma.model.Usuario;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoletaRepository extends JpaRepository<Boleta, Integer> {
    List<Boleta> findByUsuario(Usuario usuario);
}
