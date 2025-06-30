package com.ciberfarma.controller;

import com.ciberfarma.model.Usuario;
import com.ciberfarma.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // ✅ Mostrar formulario de login
    @GetMapping("/login")
    public String mostrarFormularioLogin(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/";
        }
        return "login"; // login.html
    }

    // ✅ Procesar login
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("usuario") String usuario,
                                 @RequestParam("clave") String clave,
                                 HttpSession session,
                                 Model model) {
        Usuario u = usuarioService.login(usuario, clave);
        if (u != null) {
            session.setAttribute("usuarioLogueado", u);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }
    }

    // ✅ Logout
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/usuario/login";
    }

    // ✅ Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro"; // registro.html
    }

    // ✅ Procesar registro
    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Usuario usuario,
                                    Model model) {
        if (usuarioService.existeUsuario(usuario.getUsuario())) {
            model.addAttribute("error", "Nombre de usuario ya existe");
            return "registro";
        }
        usuarioService.guardar(usuario);
        model.addAttribute("mensaje", "Registro exitoso. Ahora puede iniciar sesión.");
        return "redirect:/usuario/login";
    }

    // ✅ Mostrar formulario para editar perfil
    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/usuario/login";
        }
        model.addAttribute("usuario", usuario);
        return "perfil"; // perfil.html
    }

    // ✅ Procesar edición de perfil
    @PostMapping("/perfil")
    public String actualizarPerfil(@ModelAttribute Usuario usuarioActualizado,
                                   HttpSession session,
                                   Model model) {
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioSesion == null) {
            return "redirect:/usuario/login";
        }

        usuarioActualizado.setIdUsuario(usuarioSesion.getIdUsuario());
        usuarioActualizado.setTipo(usuarioSesion.getTipo()); // Mantener tipo
        usuarioService.guardar(usuarioActualizado);
        session.setAttribute("usuarioLogueado", usuarioActualizado);
        model.addAttribute("mensaje", "Perfil actualizado con éxito");
        return "perfil";
    }
}
