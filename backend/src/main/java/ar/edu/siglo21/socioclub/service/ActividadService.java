package ar.edu.siglo21.socioclub.service;

import ar.edu.siglo21.socioclub.model.Actividad;
import ar.edu.siglo21.socioclub.model.EstadoInscripcion;
import ar.edu.siglo21.socioclub.repository.ActividadRepository;
import ar.edu.siglo21.socioclub.repository.InscripcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

// @Service: Spring crea UN objeto de esta clase (un bean) y lo administra.
// Acá viven las reglas de negocio; el controlador solo llama a estos métodos.
@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final InscripcionRepository inscripcionRepository;

    // INYECCIÓN DE DEPENDENCIAS por constructor: no hacemos "new" de los
    // repositorios; Spring los crea y nos los pasa al construir el servicio.
    public ActividadService(ActividadRepository actividadRepository,
                            InscripcionRepository inscripcionRepository) {
        this.actividadRepository = actividadRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    // Lista todas las actividades (o solo las de una disciplina) con su cupo disponible.
    @Transactional(readOnly = true)
    public List<Actividad> listar(String disciplina) {
        List<Actividad> actividades;
        if (disciplina == null || disciplina.isBlank()) {
            actividades = actividadRepository.findAllByOrderByNombreAsc();
        } else {
            actividades = actividadRepository.findByDisciplinaOrderByNombreAsc(disciplina);
        }
        actividades.forEach(this::calcularCupoDisponible);
        return actividades;
    }

    // Disciplinas sin repetir, ordenadas, para el filtro del listado.
    @Transactional(readOnly = true)
    public List<String> listarDisciplinas() {
        return actividadRepository.findAll().stream()
                .map(Actividad::getDisciplina)
                .distinct()
                .sorted()
                .toList();
    }

    @Transactional(readOnly = true)
    public Actividad buscarPorId(Long id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la actividad con id " + id + "."));
        calcularCupoDisponible(actividad);
        return actividad;
    }

    // Alta (si no tiene id) o modificación (si tiene id).
    @Transactional
    public Actividad guardar(Actividad datos) {
        validar(datos);

        if (datos.getId() == null) {
            return actividadRepository.save(datos); // INSERT
        }

        // Modificación: buscamos la actividad guardada y le copiamos los datos nuevos.
        Actividad existente = actividadRepository.findById(datos.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la actividad con id " + datos.getId() + "."));

        // No se puede bajar el cupo por debajo de los inscriptos activos que ya tiene.
        long activos = contarInscriptosActivos(existente.getId());
        if (datos.getCupoMaximo() < activos) {
            throw new ReglaNegocioException("RN1",
                    "El cupo no puede ser menor a los " + activos + " inscriptos activos que ya tiene la actividad.");
        }

        existente.setNombre(datos.getNombre().trim());
        existente.setDisciplina(datos.getDisciplina().trim());
        existente.setProfesor(datos.getProfesor().trim());
        existente.setDias(datos.getDias().trim());
        existente.setHorario(datos.getHorario().trim());
        existente.setEdadMinima(datos.getEdadMinima());
        existente.setEdadMaxima(datos.getEdadMaxima());
        existente.setCupoMaximo(datos.getCupoMaximo());
        existente.setCuotaMensual(datos.getCuotaMensual());
        return actividadRepository.save(existente); // UPDATE
    }

    // Baja: solo si no tiene inscriptos activos (como pide el Anexo A para el DELETE).
    @Transactional
    public void eliminar(Long id) {
        Actividad actividad = buscarPorId(id);
        long activos = contarInscriptosActivos(id);
        if (activos > 0) {
            throw new ReglaNegocioException("BAJA",
                    "No se puede eliminar \"" + actividad.getNombre() + "\": tiene " + activos + " inscriptos activos.");
        }
        actividadRepository.delete(actividad);
    }

    // ----- Métodos privados de apoyo -----

    private long contarInscriptosActivos(Long actividadId) {
        return inscripcionRepository.contarPorActividadYEstado(actividadId, EstadoInscripcion.ACTIVA);
    }

    private void calcularCupoDisponible(Actividad actividad) {
        long activos = contarInscriptosActivos(actividad.getId());
        actividad.setCupoDisponible(actividad.getCupoMaximo() - (int) activos);
    }

    // Validaciones de datos de la actividad antes de guardar.
    private void validar(Actividad a) {
        if (estaVacio(a.getNombre()) || estaVacio(a.getDisciplina()) || estaVacio(a.getProfesor())
                || estaVacio(a.getDias()) || estaVacio(a.getHorario())) {
            throw new ReglaNegocioException("DATOS", "Completá todos los campos de texto.");
        }
        if (a.getEdadMinima() == null || a.getEdadMaxima() == null
                || a.getCupoMaximo() == null || a.getCuotaMensual() == null) {
            throw new ReglaNegocioException("DATOS", "Completá las edades, el cupo y la cuota.");
        }
        if (a.getEdadMinima() < 0 || a.getEdadMinima() > a.getEdadMaxima()) {
            throw new ReglaNegocioException("DATOS", "La edad mínima no puede ser mayor que la edad máxima.");
        }
        if (a.getCupoMaximo() <= 0) {
            throw new ReglaNegocioException("DATOS", "El cupo máximo tiene que ser mayor a 0.");
        }
        if (a.getCuotaMensual().compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException("DATOS", "La cuota mensual no puede ser negativa.");
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
