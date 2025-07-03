	package com.ciberfarma.controller;
	
	import com.ciberfarma.model.Producto;
	import com.ciberfarma.model.Usuario;
	import com.ciberfarma.service.CategoriaService;
	import com.ciberfarma.service.ProductoService;
	import jakarta.servlet.http.HttpSession;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.*;
	import org.springframework.web.multipart.MultipartFile;
	import org.springframework.web.servlet.mvc.support.RedirectAttributes;
	
	import java.io.IOException;
	import java.io.InputStream;
	import java.nio.file.*;
	import java.util.List;
	
	@Controller
	@RequestMapping("/productos")
	public class ProductoController {
	
	    @Autowired
	    private ProductoService productoService;
	
	    @Autowired
	    private CategoriaService categoriaService;
	    @GetMapping
	    public String listarProductos(@RequestParam(name = "buscar", required = false) String buscar,
	                                  @RequestParam(name = "pagina", defaultValue = "1") int pagina,
	                                  Model model,
	                                  HttpSession session) {
	        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
	        if (usuario == null || usuario.getIdTipo().getIdTipo() != 1) {
	            return "redirect:/acceso-denegado";
	        }
	
	        List<Producto> productos = (buscar != null && !buscar.trim().isEmpty())
	                ? productoService.buscarPorDescripcion(buscar.trim())
	                : productoService.listarTodos();
	
	        int productosPorPagina = 15;
	        int totalProductos = productos.size();
	        int totalPaginas = (int) Math.ceil((double) totalProductos / productosPorPagina);

	        if (pagina > totalPaginas) {
	            pagina = 1;
	        }

	        int inicio = (pagina - 1) * productosPorPagina;
	        int fin = Math.min(inicio + productosPorPagina, totalProductos);

	        List<Producto> productosPagina = productos.subList(inicio, fin);

	
	        Producto nuevoProducto = new Producto();
	        nuevoProducto.setIdProducto(productoService.generarSiguienteCodigo());
	
	        model.addAttribute("lstProductos", productosPagina);
	        model.addAttribute("lstCategorias", categoriaService.listarTodos());
	        model.addAttribute("producto", nuevoProducto);
	        model.addAttribute("modoEdicion", false);
	        model.addAttribute("buscar", buscar);
	        model.addAttribute("paginaActual", pagina);
	        model.addAttribute("totalPaginas", totalPaginas);
	
	        return "crudprod";
	    }
	
	
	    @GetMapping("/editar/{id}")
	    public String editarProducto(@PathVariable("id") String id,
	                                 Model model,
	                                 HttpSession session) {
	        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
	        if (usuario == null || usuario.getIdTipo().getIdTipo() != 1) {
	            return "redirect:/acceso-denegado";
	        }
	
	        Producto producto = productoService.buscarPorCodigo(id);
	        if (producto == null) {
	            return "redirect:/productos";
	        }
	
	        model.addAttribute("lstProductos", productoService.listarTodos());
	        model.addAttribute("lstCategorias", categoriaService.listarTodos());
	        model.addAttribute("producto", producto);
	        model.addAttribute("modoEdicion", true);
	
	        return "crudprod";
	    }
	
	    @PostMapping("/guardar")
	    public String guardarProducto(@ModelAttribute Producto producto,
	                                  @RequestParam("imagen") MultipartFile imagen,
	                                  RedirectAttributes redirectAttrs,
	                                  HttpSession session) {
	        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
	        if (usuario == null || usuario.getIdTipo().getIdTipo() != 1) {
	            return "redirect:/acceso-denegado";
	        }
	
	        try {
	            productoService.guardar(producto);
	
	            if (!imagen.isEmpty()) {
	                String fileName = producto.getIdProducto() + ".jpg";
	                Path uploadDir = Paths.get("uploads/productos");
	
	                if (!Files.exists(uploadDir)) {
	                    Files.createDirectories(uploadDir);
	                }
	
	                try (InputStream inputStream = imagen.getInputStream()) {
	                    Path filePath = uploadDir.resolve(fileName);
	                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
	                }
	            }
	
	            redirectAttrs.addFlashAttribute("mensaje", "Producto guardado correctamente");
	        } catch (IOException e) {
	            redirectAttrs.addFlashAttribute("error", "Error al guardar la imagen: " + e.getMessage());
	        } catch (Exception e) {
	            redirectAttrs.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
	        }
	
	        return "redirect:/productos";
	    }
	
	    @GetMapping("/eliminar/{id}")
	    public String eliminarProducto(@PathVariable("id") String id,
	                                   RedirectAttributes redirectAttrs,
	                                   HttpSession session) {
	        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
	        if (usuario == null || usuario.getIdTipo().getIdTipo() != 1) {
	            return "redirect:/acceso-denegado";
	        }
	
	        try {
	            productoService.eliminar(id);
	            redirectAttrs.addFlashAttribute("mensaje", "Producto eliminado correctamente");
	        } catch (Exception e) {
	            redirectAttrs.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
	        }
	
	        return "redirect:/productos";
	    }
	
	    @GetMapping("/producto/{id}")
	    public String verProducto(@PathVariable("id") String id, Model model) {
	        Producto producto = productoService.buscarPorCodigo(id);
	        if (producto == null) {
	            return "redirect:/catalogo";
	        }
	        model.addAttribute("producto", producto);
	        return "muestra";
	    }
	    @PostMapping("/cambiar-moneda")
	    public String cambiarMoneda(@RequestParam("moneda") String moneda, HttpSession session,
	                                 @RequestHeader(value = "referer", required = false) String referer) {
	        session.setAttribute("moneda", moneda);
	        return "redirect:" + (referer != null ? referer : "/");
	    }
	}


