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

    // Login con correo
    public Usuario login(String correo, String clave) {
        return usuarioRepository.findByCorreoAndClave(correo, clave).orElse(null);
    }

    // Guardar o actualizar usuario
    public void guardar(Usuario u) {
        usuarioRepository.save(u);
    }

    // Verificar si el correo ya existe
    public boolean existeUsuario(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    // Obtener usuario por ID
    public Optional<Usuario> obtenerPorId(int id) {
        return usuarioRepository.findById(id);
    }
}
