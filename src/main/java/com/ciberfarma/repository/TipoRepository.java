package com.ciberfarma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ciberfarma.model.Tipo;

public interface TipoRepository extends JpaRepository<Tipo, Integer> {
}
