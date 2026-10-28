package ar.edu.siglo21.socioclub.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// @Entity: esta clase se guarda en una tabla de la base de datos.
// Cada objeto Actividad es una fila; cada atributo, una columna.
@Entity
@Table(name = "actividad")
public class Actividad {

    // @Id: clave primaria. @GeneratedValue IDENTITY: MySQL la genera sola (AUTO_INCREMENT).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String disciplina;

    @Column(nullable = false, length = 120)
    private String profesor;

    @Column(nullable = false, length = 120)
    private String dias;

    @Column(nullable = false, length = 20)
    private String horario;

    @Column(name = "edad_minima", nullable = false)
    private Integer edadMinima;

    @Column(name = "edad_maxima", nullable = false)
    private Integer edadMaxima;

    @Column(name = "cupo_maximo", nullable = false)
    private Integer cupoMaximo;

    // BigDecimal: tipo recomendado para dinero (evita errores de redondeo de double).
    @Column(name = "cuota_mensual", nullable = false, precision = 12, scale = 2)
    private BigDecimal cuotaMensual;

    // Una actividad tiene muchas inscripciones.
    // mappedBy = "actividad": la clave foránea (actividad_id) la maneja el atributo
    // "actividad" de la clase Inscripcion. Acá NO se crea ninguna columna nueva.
    // cascade REMOVE: si se borra la actividad, se borran también sus inscripciones
    // históricas (las de BAJA); el servicio no deja borrarla si tiene inscriptos ACTIVOS.
    @OneToMany(mappedBy = "actividad", cascade = CascadeType.REMOVE)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    // @Transient: atributo que NO es columna. Lo calcula el servicio
    // (cupo máximo menos inscripciones activas) para mostrarlo en pantalla.
    @Transient
    private Integer cupoDisponible;

    // JPA necesita un constructor vacío.
    public Actividad() {
    }

    // ----- Getters y setters (sin Lombok, como se vio en la materia) -----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDisciplina() { return disciplina; }
    public void setDisciplina(String disciplina) { this.disciplina = disciplina; }

    public String getProfesor() { return profesor; }
    public void setProfesor(String profesor) { this.profesor = profesor; }

    public String getDias() { return dias; }
    public void setDias(String dias) { this.dias = dias; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public Integer getEdadMinima() { return edadMinima; }
    public void setEdadMinima(Integer edadMinima) { this.edadMinima = edadMinima; }

    public Integer getEdadMaxima() { return edadMaxima; }
    public void setEdadMaxima(Integer edadMaxima) { this.edadMaxima = edadMaxima; }

    public Integer getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(Integer cupoMaximo) { this.cupoMaximo = cupoMaximo; }

    public BigDecimal getCuotaMensual() { return cuotaMensual; }
    public void setCuotaMensual(BigDecimal cuotaMensual) { this.cuotaMensual = cuotaMensual; }

    public List<Inscripcion> getInscripciones() { return inscripciones; }
    public void setInscripciones(List<Inscripcion> inscripciones) { this.inscripciones = inscripciones; }

    public Integer getCupoDisponible() { return cupoDisponible; }
    public void setCupoDisponible(Integer cupoDisponible) { this.cupoDisponible = cupoDisponible; }
}
