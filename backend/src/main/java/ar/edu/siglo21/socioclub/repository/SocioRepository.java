package ar.edu.siglo21.socioclub.repository;

import ar.edu.siglo21.socioclub.model.Socio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Long> {

    // findBy + Apellido + Containing (LIKE '%texto%') + IgnoreCase (sin importar mayúsculas)
    // Con Pageable devuelve una "página" de resultados (para el listado paginado, semana 2).
    Page<Socio> findByApellidoContainingIgnoreCase(String apellido, Pageable pageable);

    // Para el ingreso del socio a la app con su DNI (Parte C y D).
    Optional<Socio> findByDni(String dni);
}
