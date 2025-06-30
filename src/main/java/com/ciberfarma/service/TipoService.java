package com.ciberfarma.service;

import com.ciberfarma.model.Tipo;
import com.ciberfarma.repository.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoService {

    @Autowired
    private TipoRepository tipoRepository;

    public List<Tipo> listarTodos() {
        return tipoRepository.findAll();
    }

    public Tipo obtenerPorId(int id) {
        return tipoRepository.findById(id).orElse(null);
    }
}
