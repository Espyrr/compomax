package com.ciberfarma.service;

import com.ciberfarma.model.Boleta;
import com.ciberfarma.model.Usuario;
import com.ciberfarma.repository.BoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    public List<Boleta> listarTodos() {
        return boletaRepository.findAll();
    }
    public List<Boleta> listarPorUsuario(Usuario usuario) {
        return boletaRepository.findByUsuario(usuario);
    }

    public Optional<Boleta> obtenerPorId(Integer id) {
        return boletaRepository.findById(id);
    }

    public Boleta guardar(Boleta boleta) {
        boleta.setFechaBoleta(LocalDateTime.now());
        return boletaRepository.save(boleta);
    }

    public void eliminar(Integer id) {
        boletaRepository.deleteById(id);
    }
}
