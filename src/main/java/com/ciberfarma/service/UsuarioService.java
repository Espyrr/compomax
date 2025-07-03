package com.ciberfarma.service;

import com.ciberfarma.model.Tipo;
import com.ciberfarma.model.Usuario;
import com.ciberfarma.repository.TipoRepository;
import com.ciberfarma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoRepository tipoRepository;

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

    public Optional<Usuario> obtenerPorId(int id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Eliminar usuario 
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    // Listar todos los tipos 
    public List<Tipo> listarTipos() {
        return tipoRepository.findAll();
    }
}
