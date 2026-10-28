package ar.edu.siglo21.socioclub.controller;

import ar.edu.siglo21.socioclub.model.Actividad;
import ar.edu.siglo21.socioclub.service.ActividadService;
import ar.edu.siglo21.socioclub.service.RecursoNoEncontradoException;
import ar.edu.siglo21.socioclub.service.ReglaNegocioException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// @Controller: controlador MVC. Recibe pedidos del navegador, le pide al
// SERVICIO que haga el trabajo y elige qué vista Thymeleaf mostrar.
// No tiene reglas de negocio ni accede al repositorio.
@Controller
@RequestMapping("/actividades")
public class ActividadController {

    private final ActividadService actividadService;

    // Spring inyecta el servicio (bean) por el constructor.
    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    // GET /actividades  (o /actividades?disciplina=Fútbol)
    @GetMapping
    public String listar(@RequestParam(required = false) String disciplina, Model model) {
        model.addAttribute("actividades", actividadService.listar(disciplina));
        model.addAttribute("disciplinas", actividadService.listarDisciplinas());
        model.addAttribute("disciplinaElegida", disciplina);
        return "actividades/lista"; // -> templates/actividades/lista.html
    }

    // GET /actividades/nueva: formulario vacío
    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("actividad", new Actividad());
        return "actividades/formulario";
    }

    // GET /actividades/3/editar: formulario con los datos de la actividad 3
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("actividad", actividadService.buscarPorId(id));
            return "actividades/formulario";
        } catch (RecursoNoEncontradoException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/actividades";
        }
    }

    // POST /actividades/guardar: recibe el formulario (alta o modificación)
    // @ModelAttribute: Spring arma un objeto Actividad con los campos del formulario.
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("actividad") Actividad actividad,
                          BindingResult resultado,
                          Model model,
                          RedirectAttributes redirect) {

        // Si escribieron texto donde va un número, Spring no puede convertirlo.
        if (resultado.hasErrors()) {
            model.addAttribute("error", "Revisá los campos numéricos: edades, cupo y cuota tienen que ser números.");
            return "actividades/formulario";
        }

        try {
            boolean esNueva = actividad.getId() == null;
            actividadService.guardar(actividad); // las validaciones están en el servicio
            redirect.addFlashAttribute("exito",
                    esNueva ? "Actividad creada correctamente." : "Actividad modificada correctamente.");
            return "redirect:/actividades";
        } catch (ReglaNegocioException | RecursoNoEncontradoException e) {
            // Volvemos al formulario con lo que escribió Marta y el mensaje del error.
            model.addAttribute("error", e.getMessage());
            return "actividades/formulario";
        }
    }

    // POST /actividades/3/eliminar
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            actividadService.eliminar(id);
            redirect.addFlashAttribute("exito", "Actividad eliminada.");
        } catch (ReglaNegocioException | RecursoNoEncontradoException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/actividades";
    }
}
