package ar.edu.siglo21.socioclub.repository;

import ar.edu.siglo21.socioclub.model.EstadoInscripcion;
import ar.edu.siglo21.socioclub.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    // Consulta escrita a mano con @Query, en JPQL: se parece a SQL pero usa
    // los nombres de las CLASES y ATRIBUTOS de Java, no los de las tablas.
    // Cuenta las inscripciones de una actividad en un estado (por ejemplo ACTIVA).
    // Sirve para calcular el cupo disponible y para la regla RN1.
    @Query("SELECT COUNT(i) FROM Inscripcion i " +
           "WHERE i.actividad.id = :actividadId AND i.estado = :estado")
    long contarPorActividadYEstado(@Param("actividadId") Long actividadId,
                                   @Param("estado") EstadoInscripcion estado);

    // Métodos derivados que se usan en la semana 2 (reglas RN2 y RN5):
    boolean existsBySocioIdAndActividadIdAndEstado(Long socioId, Long actividadId, EstadoInscripcion estado);

    long countBySocioIdAndEstado(Long socioId, EstadoInscripcion estado);

    List<Inscripcion> findByActividadIdAndEstado(Long actividadId, EstadoInscripcion estado);
}
