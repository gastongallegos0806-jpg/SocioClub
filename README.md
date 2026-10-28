# SocioClub: Club Social y Deportivo Los Algarrobos

Trabajo Práctico Integrador de Taller de Construcción de Software (Universidad Siglo 21).

**Integrantes:** _(completar con nombre y apellido de cada uno)_

## Estructura del repositorio

| Carpeta | Qué contiene | Parte del TP |
|---|---|---|
| `/sitio-publico` | Sitio del club en HTML5, CSS3 y JavaScript puro | A |
| `/backend` | Proyecto Spring Boot (Thymeleaf + JPA + MySQL), luego también la API REST | B y C |
| `/app-socios` | App "Mi Club" en React (se arma en la semana 2) | D |
| `/base-datos` | Script `socioclub.sql` con la creación de tablas y los datos iniciales | Entrega |

## Requisitos para levantar el proyecto

Java JDK 17 o superior, MySQL 8 y un editor (IntelliJ IDEA o VS Code con el *Extension Pack for Java*).

## 1. Sitio público (Parte A)

No necesita instalación. Abrir `sitio-publico/index.html` en el navegador, o con la extensión *Live Server* de VS Code (clic derecho sobre `index.html` → *Open with Live Server*).

## 2. Backend Spring Boot (Parte B)

### Primera vez: generar el proyecto con Spring Initializr

El enunciado pide que el proyecto se genere con Spring Initializr. Además, eso agrega el archivo `mvnw`, que permite compilar sin instalar Maven.

1. Entrar a https://start.spring.io y elegir: **Maven**, **Java**, la versión **3.x** estable que aparezca, Group `ar.edu.siglo21`, Artifact `socioclub`, Packaging **Jar**, Java **17** (o la que tengan instalada).
2. Agregar las dependencias **Spring Web**, **Thymeleaf**, **Spring Data JPA** y **MySQL Driver**.
3. Apretar *Generate*, descomprimir el zip y copiar su contenido dentro de `/backend`.
4. Reemplazar la carpeta `src` y el archivo `pom.xml` generados por los de este repositorio.

### Configurar la contraseña de MySQL

En `backend/src/main/resources/application.properties`, cambiar `CAMBIAR_CONTRASENA` por la contraseña del usuario `root` de MySQL. **No hace falta crear la base a mano:** se crea sola al arrancar.

### Levantar el backend

Desde la carpeta `/backend`:

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / Mac
./mvnw spring-boot:run
```

O desde el editor: abrir `SocioclubApplication.java` y apretar *Run*.

Cuando en la consola aparezca `Started SocioclubApplication`, abrir **http://localhost:8080** en el navegador.

**Importante:** con `spring.jpa.hibernate.ddl-auto=create`, cada vez que se reinicia el backend las tablas se borran y se vuelven a cargar con los datos iniciales. Así la demo siempre arranca igual.

## Datos de prueba

Al arrancar se cargan las 8 actividades del enunciado, 20 socios y 20 inscripciones.

| Caso para demostrar | Socio / actividad | DNI |
|---|---|---|
| Socio MOROSO (RN4) | María López | 38123456 |
| Socio MOROSO (RN4) | Bruno Suárez | 33123456 |
| Socio con 3 actividades (RN5) | Pedro Díaz | 42123456 |
| Socio con 2 actividades (RN6, 10 % de descuento) | Lucía Pérez | 40123456 |
| Socio de 11 años (RN3, fuera de rango en Vóley) | Tomás Gómez | 45123456 |
| Actividad con cupo casi completo (RN1) | Yoga: 14 de 15 ocupados | |
| Actividad que no se puede eliminar | Yoga (tiene inscriptos activos) | |

## Estado del proyecto (hito semana 1)

- Parte A completa: tres páginas, catálogo generado con JavaScript desde la clase `Actividad`, filtros por disciplina y edad, formulario validado sin `alert()` y botón "Volver" con `history.back()`.
- Parte B: proyecto en capas (`model`, `repository`, `service`, `controller`), entidades `Socio`, `Actividad` e `Inscripcion` con sus relaciones, repositorios con métodos derivados y `@Query`, datos iniciales cargados y ABM de actividades en Thymeleaf con cupo disponible.

### Plan para la semana 2

ABM de socios con paginación y búsqueda por apellido; inscripciones con las reglas RN1 a RN5 en `InscripcionService`; listado de inscriptos por actividad; inicio de la app React con create-react-app, React Router y datos locales.
