package ar.edu.siglo21.socioclub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "socio")
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // unique = true: no puede haber dos socios con el mismo DNI.
    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false, length = 180)
    private String email;

    @Column(length = 30)
    private String telefono;

    // EnumType.STRING: guarda el texto "AL_DIA" o "MOROSO" (no un número).
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuota", nullable = false, length = 10)
    private EstadoCuota estadoCuota = EstadoCuota.AL_DIA;

    // Un socio tiene muchas inscripciones (la FK socio_id está en Inscripcion).
    @OneToMany(mappedBy = "socio")
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Socio() {
    }

    // Edad calculada a la fecha de hoy (la va a usar la regla RN3).
    public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public EstadoCuota getEstadoCuota() { return estadoCuota; }
    public void setEstadoCuota(EstadoCuota estadoCuota) { this.estadoCuota = estadoCuota; }

    public List<Inscripcion> getInscripciones() { return inscripciones; }
    public void setInscripciones(List<Inscripcion> inscripciones) { this.inscripciones = inscripciones; }
}
