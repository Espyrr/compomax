package com.ciberfarma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ciberfarma.model.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {
}
