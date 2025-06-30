package com.ciberfarma.controller;

import com.ciberfarma.model.Tipo;
import com.ciberfarma.model.Usuario;
import com.ciberfarma.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarFormularioLogin(HttpSession session,
                                         Model model,
                                         @ModelAttribute("mensaje") String mensaje) {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/";
        }

        // Si hay un mensaje flash, pásalo explícitamente al modelo
        if (mensaje != null && !mensaje.isEmpty()) {
            model.addAttribute("mensaje", mensaje);
        }

        return "login";
    }



    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correo") String correo,
                                @RequestParam("clave") String clave,
                                HttpSession session,
                                Model model) {
        Usuario u = usuarioService.login(correo, clave);
        if (u != null) {
            session.setAttribute("usuarioLogueado", u);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }
    }
    // logout
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // formulario de registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setIdTipo(new Tipo());
        model.addAttribute("usuario", nuevoUsuario);
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("usuario") Usuario usuario,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "registro";
        }

        if (usuarioService.existeUsuario(usuario.getCorreo())) {
            model.addAttribute("error", "El correo ya está registrado");
            return "registro";
        }

        Tipo tipoCliente = new Tipo();
        tipoCliente.setIdTipo(2);
        usuario.setIdTipo(tipoCliente);

        usuarioService.guardar(usuario);

        redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso. Ahora puede iniciar sesión.");
        return "redirect:/usuario/login";
    }



    // formulario de perfil
    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/usuario/login";
        }
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    // actualizar perfil
    @PostMapping("/perfil")
    public String actualizarPerfil(@ModelAttribute Usuario usuarioActualizado,
                                   HttpSession session,
                                   Model model) {
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioSesion == null) {
            return "redirect:/usuario/login";
        }

        usuarioActualizado.setIdUsuario(usuarioSesion.getIdUsuario());
        usuarioActualizado.setIdTipo(usuarioSesion.getIdTipo()); // mantener tipo
        usuarioService.guardar(usuarioActualizado);
        session.setAttribute("usuarioLogueado", usuarioActualizado);
        model.addAttribute("mensaje", "Perfil actualizado con éxito");
        return "perfil";
    }
}

