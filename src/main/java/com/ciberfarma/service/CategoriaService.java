package com.ciberfarma.service;

import com.ciberfarma.model.Categoria;
import com.ciberfarma.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> obtenerPorId(int id) {
        return categoriaRepository.findById(id);
    }

    public void guardar(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    public void eliminar(int id) {
        categoriaRepository.deleteById(id);
    }
} 
