package com.ciberfarma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ciberfarma.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
