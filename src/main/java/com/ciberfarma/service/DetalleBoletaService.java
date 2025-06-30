package com.ciberfarma.service;

import com.ciberfarma.model.DetalleBoleta;
import com.ciberfarma.repository.DetalleBoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DetalleBoletaService {

    @Autowired
    private DetalleBoletaRepository detalleBoletaRepository;

    public void guardarDetalles(List<DetalleBoleta> detalles) {
        for (DetalleBoleta det : detalles) {
            BigDecimal importe = det.getPrecioUnitario().multiply(new BigDecimal(det.getCantidad()));
            det.setImporte(importe);
            detalleBoletaRepository.save(det);
        }
    }
}
