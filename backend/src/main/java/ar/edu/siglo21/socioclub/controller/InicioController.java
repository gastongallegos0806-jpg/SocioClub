package ar.edu.siglo21.socioclub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    // Si Marta entra a http://localhost:8080/ la mandamos al listado de actividades.
    @GetMapping("/")
    public String inicio() {
        return "redirect:/actividades";
    }
}
