package com.ciberfarma.service;

import com.ciberfarma.model.Estado;
import com.ciberfarma.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public List<Estado> listarTodos() {
        return estadoRepository.findAll();
    }

    public Optional<Estado> obtenerPorId(int id) {
        return estadoRepository.findById(id);
    }

    public void guardar(Estado estado) {
        estadoRepository.save(estado);
    }

    public void eliminar(int id) {
        estadoRepository.deleteById(id);
    }
}
