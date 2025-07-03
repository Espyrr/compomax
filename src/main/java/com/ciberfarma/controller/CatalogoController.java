package com.ciberfarma.controller;

import com.ciberfarma.model.Producto;
import com.ciberfarma.service.ProductoService;
import com.ciberfarma.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String mostrarCatalogo(@RequestParam(name = "buscar", required = false) String buscar,
                                  @RequestParam(name = "tipoOrden", required = false) String tipoOrden,
                                  @RequestParam(name = "categoria", required = false) Integer idCategoria,
                                  @RequestParam(name = "pagina", defaultValue = "1") int pagina,
                                  Model model) {

        List<Producto> productos = productoService.obtenerFiltrados(tipoOrden, buscar, idCategoria);

        int productosPorPagina = 15;
        int totalProductos = productos.size();
        int totalPaginas = (int) Math.ceil((double) totalProductos / productosPorPagina);
        int inicio = (pagina - 1) * productosPorPagina;
        int fin = Math.min(inicio + productosPorPagina, totalProductos);
        List<Producto> productosPagina = productos.subList(inicio, fin);

        model.addAttribute("lstProductos", productosPagina);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", totalPaginas);
        model.addAttribute("busqueda", buscar); // 🔄 nombre coherente con el HTML
        model.addAttribute("nroResultados", productos.size()); // 🔄 para mostrar (xx)
        model.addAttribute("tipoOrden", tipoOrden);
        model.addAttribute("categoria", idCategoria);
        model.addAttribute("lstCategorias", categoriaService.listarTodos());

        return "catalogo";
    }

}

