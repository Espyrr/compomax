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

    // ✅ Reemplazamos el nombre para que coincida con el del controller
    public Producto buscarPorCodigo(String id) {
        return productoRepository.findById(id).orElse(null);
    }

    // ✅ Agregamos búsqueda por descripción (para el filtro)
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

        // Filtro por categoría (mutable)
        if (idCategoria != null) {
            productos = productos.stream()
                    .filter(p -> p.getIdCategoria().getIdCategoria().equals(idCategoria))
                    .collect(Collectors.toList()); // ✅ mutable
        }

        // Ordenamiento (solo funciona si la lista es mutable)
        if ("precioAsc".equals(tipoOrden)) {
            productos.sort(Comparator.comparing(Producto::getPrecio));
        } else if ("precioDesc".equals(tipoOrden)) {
            productos.sort(Comparator.comparing(Producto::getPrecio).reversed());
        }

        return productos;
    }


}
