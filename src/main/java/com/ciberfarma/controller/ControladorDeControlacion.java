package com.ciberfarma.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorDeControlacion {

    @GetMapping("/")
    public String mostrarInicio() {
        return "index"; 
    }
}
