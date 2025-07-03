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
                .limit(6)
                .collect(Collectors.toList());

        // TARJETAS DE VIDEO - idCategoria = 2
        List<Producto> tarjetas = productoService.listarTodos()
                .stream()
                .filter(p -> p.getIdCategoria().getIdCategoria() == 2)
                .collect(Collectors.toList());

        // MONITORES - idCategoria = 8
        List<Producto> monitores = productoService.listarTodos()
                .stream()
                .filter(p -> p.getIdCategoria().getIdCategoria() == 8)
                .collect(Collectors.toList());

        // PROCESADORES - idCategoria = 1
        List<Producto> procesadores = productoService.listarTodos()
                .stream()
                .filter(p -> p.getIdCategoria().getIdCategoria() == 1)
                .collect(Collectors.toList());

        // PASAR LAS VARIABLES CORRECTAS AL MODELO
        model.addAttribute("nuevos", nuevos);
        model.addAttribute("tarjetas", tarjetas);
        model.addAttribute("monitores", monitores);
        model.addAttribute("procesadores", procesadores);

        return "index";
    }
}

