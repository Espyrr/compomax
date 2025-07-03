package com.ciberfarma.controller;

import com.ciberfarma.model.Producto;
import com.ciberfarma.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/")
    public String mostrarInicio(Model model) {

        // NUEVOS INGRESOS
        List<Producto> nuevos = productoService.listarTodos()
                .stream()
                .sorted((a, b) -> b.getIdProducto().compareTo(a.getIdProducto()))
                .limit(10)
                .collect(Collectors.toList());

        List<Producto> tarjetas = productoService.listarTodos()
                .stream()
                .filter(p -> p.getIdCategoria().getIdCategoria() == 2)
                .limit(10)
                .collect(Collectors.toList());

        List<Producto> monitores = productoService.listarTodos()
                .stream()
                .filter(p -> p.getIdCategoria().getIdCategoria() == 8)
                .limit(10)
                .collect(Collectors.toList());

        List<Producto> procesadores = productoService.listarTodos()
                .stream()
                .filter(p -> p.getIdCategoria().getIdCategoria() == 1)
                .limit(10)
                .collect(Collectors.toList());

        model.addAttribute("nuevos", nuevos);
        model.addAttribute("tarjetas", tarjetas);
        model.addAttribute("monitores", monitores);
        model.addAttribute("procesadores", procesadores);

        return "index";
    }
}

