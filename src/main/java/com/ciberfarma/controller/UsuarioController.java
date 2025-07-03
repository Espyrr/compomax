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

import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // --- LOGIN Y PERFIL (NO CAMBIA) ---
    @GetMapping("/login")
    public String mostrarFormularioLogin(HttpSession session,
                                         Model model,
                                         @ModelAttribute("mensaje") String mensaje) {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/";
        }

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

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/usuario/login";
        }
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PostMapping("/perfil")
    public String actualizarPerfil(@ModelAttribute Usuario usuarioActualizado,
                                   HttpSession session,
                                   Model model) {
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioSesion == null) {
            return "redirect:/usuario/login";
        }

        usuarioActualizado.setIdUsuario(usuarioSesion.getIdUsuario());
        usuarioActualizado.setIdTipo(usuarioSesion.getIdTipo());
        usuarioService.guardar(usuarioActualizado);
        session.setAttribute("usuarioLogueado", usuarioActualizado);
        model.addAttribute("mensaje", "Perfil actualizado con éxito");
        return "perfil";
    }

    // --- REGISTRO USUARIO NORMAL (NO ADMIN) ---
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
        tipoCliente.setIdTipo(2); // Tipo Cliente
        usuario.setIdTipo(tipoCliente);

        usuarioService.guardar(usuario);
        redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso. Ahora puede iniciar sesión.");
        return "redirect:/usuario/login";
    }

    // --- CRUD ADMIN: USUARIOS ---
    @GetMapping
    public String listarUsuarios(@RequestParam(name = "pagina", defaultValue = "1") int pagina,
                                 Model model, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u == null || u.getIdTipo().getIdTipo() != 1) {
            return "redirect:/acceso-denegado";
        }

        List<Usuario> usuarios = usuarioService.listarTodos();
        int porPagina = 10;
        int total = usuarios.size();
        int totalPaginas = (int) Math.ceil((double) total / porPagina);
        int inicio = (pagina - 1) * porPagina;
        int fin = Math.min(inicio + porPagina, total);
        List<Usuario> usuariosPagina = usuarios.subList(inicio, fin);

        model.addAttribute("lstUsuarios", usuariosPagina);
        model.addAttribute("lstTipos", usuarioService.listarTipos());
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("modoEdicion", false);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", totalPaginas);

        return "crudusuario";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u == null || u.getIdTipo().getIdTipo() != 1) {
            return "redirect:/acceso-denegado";
        }

        Usuario usuario = usuarioService.obtenerPorId(id).orElse(null);
        if (usuario == null) {
            return "redirect:/usuario";
        }

        model.addAttribute("lstUsuarios", usuarioService.listarTodos());
        model.addAttribute("lstTipos", usuarioService.listarTipos());
        model.addAttribute("usuario", usuario);
        model.addAttribute("modoEdicion", true);
        return "crudusuario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario,
                                 RedirectAttributes redirect) {
        try {
            usuarioService.guardar(usuario);
            redirect.addFlashAttribute("mensaje", "Usuario guardado correctamente.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/usuario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Integer id,
                                  RedirectAttributes redirect, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u == null || u.getIdTipo().getIdTipo() != 1) {
            return "redirect:/acceso-denegado";
        }

        try {
            usuarioService.eliminar(id);
            redirect.addFlashAttribute("mensaje", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/usuario";
    }
}

