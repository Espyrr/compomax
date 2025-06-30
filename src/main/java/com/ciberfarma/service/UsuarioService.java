package com.ciberfarma.service;

import com.ciberfarma.model.Usuario;
import com.ciberfarma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Login
    public Usuario login(String usuario, String clave) {
        return usuarioRepository.findByUsuarioAndClave(usuario, clave).orElse(null);
    }

    // Guardar o actualizar usuario
    public void guardar(Usuario u) {
        usuarioRepository.save(u);
    }

    // Verificar si el nombre de usuario ya existe
    public boolean existeUsuario(String usuario) {
        return usuarioRepository.existsByUsuario(usuario);
    }

    // Obtener por ID (opcional si lo usas)
    public Optional<Usuario> obtenerPorId(int id) {
        return usuarioRepository.findById(id);
    }
}
