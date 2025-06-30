package com.ciberfarma.controller;

import com.ciberfarma.model.Producto;
import com.ciberfarma.model.Categoria;
import com.ciberfarma.model.Usuario;
import com.ciberfarma.service.ProductoService;
import com.ciberfarma.service.CategoriaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private static final String UPLOAD_DIR = "src/main/resources/static/img/productos/";

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    // Listar productos y cargar vista de mantenimiento
    @GetMapping
    public String listarProductos(@RequestParam(name = "buscar", required = false) String buscar,
                                   Model model,
                                   HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getTipo().getIdTipo() != 1) {
            return "redirect:/acceso-denegado";
        }

        List<Producto> productos;
        if (buscar != null && !buscar.trim().isEmpty()) {
            productos = productoService.buscarPorDescripcion(buscar.trim());
            model.addAttribute("buscar", buscar);
        } else {
            productos = productoService.listarTodos();
        }

        List<Categoria> categorias = categoriaService.listarTodos();

        model.addAttribute("lstProductos", productos);
        model.addAttribute("lstCategorias", categorias);

        return "crudproductos";
    }

    // Mostrar formulario para nuevo producto
    @GetMapping("/nuevo")
    public String nuevoProducto(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getTipo().getIdTipo() != 1) {
            return "redirect:/acceso-denegado";
        }

        model.addAttribute("producto", new Producto());
        model.addAttribute("lstCategorias", categoriaService.listarTodos());
        return "formproducto";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable("id") String id,
                                 Model model,
                                 HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getTipo().getIdTipo() != 1) {
            return "redirect:/acceso-denegado";
        }

        Producto producto = productoService.buscarPorCodigo(id);
        if (producto == null) {
            return "redirect:/productos";
        }

        model.addAttribute("producto", producto);
        model.addAttribute("lstCategorias", categoriaService.listarTodos());
        return "formproducto";
    }

    // Guardar producto (nuevo o actualizado)
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto,
                                  @RequestParam("archivo") MultipartFile archivo,
                                  RedirectAttributes redirectAttrs,
                                  HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getTipo().getIdTipo() != 1) {
            return "redirect:/acceso-denegado";
        }

        try {
            String codigo = producto.getIdProducto();

            if (!archivo.isEmpty()) {
                File dir = new File(UPLOAD_DIR);
                if (!dir.exists()) dir.mkdirs();

                File oldFile = new File(UPLOAD_DIR + codigo + ".jpg");
                if (oldFile.exists()) oldFile.delete();

                String fileName = StringUtils.cleanPath(codigo + ".jpg");
                archivo.transferTo(new File(UPLOAD_DIR + fileName));
            }

            productoService.guardar(producto);
            redirectAttrs.addFlashAttribute("mensaje", "Producto guardado correctamente");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/productos";
    }

    // Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") String id,
                                   RedirectAttributes redirectAttrs,
                                   HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || usuario.getTipo().getIdTipo() != 1) {
            return "redirect:/acceso-denegado";
        }

        try {
            productoService.eliminar(id);

            File imgFile = new File(UPLOAD_DIR + id + ".jpg");
            if (imgFile.exists()) imgFile.delete();

            redirectAttrs.addFlashAttribute("mensaje", "Producto eliminado correctamente");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }

        return "redirect:/productos";
    }
    
    @GetMapping("/producto/{id}")
    public String verProducto(@PathVariable("id") String id,
                              Model model) {
        Producto producto = productoService.buscarPorCodigo(id);
        if (producto == null) {
            return "redirect:/catalogo"; // o una página 404 si deseas
        }
        model.addAttribute("producto", producto);
        return "compra"; // compra.html
    }

}
