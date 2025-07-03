package com.ciberfarma.service;

import com.ciberfarma.model.Producto;
import com.ciberfarma.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorCodigo(String id) {
        return productoRepository.findById(id).orElse(null);
    }

    public List<Producto> buscarPorDescripcion(String descripcion) {
        return productoRepository.findByDescripcionContainingIgnoreCase(descripcion);
    }

    public void guardar(Producto producto) {
        productoRepository.save(producto);
    }

    public void eliminar(String id) {
        productoRepository.deleteById(id);
    }
    public List<Producto> obtenerFiltrados(String tipoOrden, String buscar, Integer idCategoria) {
        List<Producto> productos;

        if (buscar != null && !buscar.isBlank()) {
            productos = productoRepository.findByDescripcionContainingIgnoreCase(buscar);
        } else {
            productos = productoRepository.findAll();
        }

        if (idCategoria != null) {
            productos = productos.stream()
                    .filter(p -> p.getIdCategoria().getIdCategoria().equals(idCategoria))
                    .collect(Collectors.toList()); 
        }

        if ("precioAsc".equals(tipoOrden)) {
            productos.sort(Comparator.comparing(Producto::getPrecio));
        } else if ("precioDesc".equals(tipoOrden)) {
            productos.sort(Comparator.comparing(Producto::getPrecio).reversed());
        }

        return productos;
    }
    public String generarSiguienteCodigo() {
        List<String> codigosExistentes = productoRepository.findAll().stream()
            .map(Producto::getIdProducto)
            .filter(cod -> cod.matches("[Pp]\\d{4}"))
            .map(cod -> cod.toUpperCase())
            .collect(Collectors.toList());

        for (int i = 1; i <= 9999; i++) {
            String codigo = String.format("P%04d", i);
            if (!codigosExistentes.contains(codigo)) {
                return codigo;
            }
        }

        return null;
    }



}
