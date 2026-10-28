package ar.edu.siglo21.socioclub.repository;

import ar.edu.siglo21.socioclub.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Al extender JpaRepository<Actividad, Long> ya tenemos, sin escribir nada:
// findAll(), findById(id), save(objeto), deleteById(id), count(), etc.
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    // Método DERIVADO DEL NOMBRE: Spring lee el nombre y arma el SQL solo.
    // findAll + OrderBy + Nombre + Asc  ->  SELECT * FROM actividad ORDER BY nombre ASC
    List<Actividad> findAllByOrderByNombreAsc();

    // findBy + Disciplina + OrderBy Nombre Asc
    // ->  SELECT * FROM actividad WHERE disciplina = ? ORDER BY nombre ASC
    List<Actividad> findByDisciplinaOrderByNombreAsc(String disciplina);
}
