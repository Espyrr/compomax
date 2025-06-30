package com.ciberfarma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ciberfarma.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, String> {
	List<Producto> findByDescripcionContainingIgnoreCase(String descripcion);
}
