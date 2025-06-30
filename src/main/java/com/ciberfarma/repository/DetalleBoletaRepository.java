package com.ciberfarma.repository;

import com.ciberfarma.model.DetalleBoleta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, Integer> {
    List<DetalleBoleta> findByBoletaIdBoleta(Integer idBoleta); 
}
