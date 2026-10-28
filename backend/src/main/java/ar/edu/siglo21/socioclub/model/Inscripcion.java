package ar.edu.siglo21.socioclub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

// Tabla intermedia entre Socio y Actividad, con datos propios (fecha y estado).
@Entity
@Table(name = "inscripcion")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchas inscripciones pertenecen a un socio.
    // @JoinColumn: en la tabla inscripcion se crea la columna socio_id (clave foránea).
    @ManyToOne(optional = false)
    @JoinColumn(name = "socio_id", nullable = false)
    private Socio socio;

    // Muchas inscripciones pertenecen a una actividad (columna actividad_id).
    @ManyToOne(optional = false)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EstadoInscripcion estado = EstadoInscripcion.ACTIVA;

    public Inscripcion() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Socio getSocio() { return socio; }
    public void setSocio(Socio socio) { this.socio = socio; }

    public Actividad getActividad() { return actividad; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }

    public LocalDate getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(LocalDate fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }

    public EstadoInscripcion getEstado() { return estado; }
    public void setEstado(EstadoInscripcion estado) { this.estado = estado; }
}
