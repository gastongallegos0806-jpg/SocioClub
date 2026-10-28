# Guía de estudio para la defensa (semana 1)

Esta guía recorre el código en el orden en que conviene leerlo. Para cada archivo dice qué hace y qué frase usar si el docente pregunta por él. Al final están los cambios en vivo más probables.

---

## Parte A: sitio público

### Cómo está organizado

Tres páginas HTML que comparten **un solo** `estilos.css` y **un solo** `script.js`. El script sabe en qué página está porque busca un elemento: si encuentra `#lista-actividades`, arma el catálogo; si encuentra `#form-preinscripcion`, activa la validación. Si no lo encuentra, esa parte no hace nada.

### `index.html`, `actividades.html`, `preinscripcion.html`

Cada etiqueta semántica tiene un motivo:

| Etiqueta | Dónde se usa | Por qué |
|---|---|---|
| `header` | Logo y menú arriba | Es el encabezado de la página |
| `nav` | El menú | Es la navegación principal |
| `main` | Contenido central | Lo propio de cada página (hay uno solo) |
| `section` | Historia, filtros, formulario | Bloques temáticos |
| `article` | Cada tarjeta de actividad | Contenido independiente, tiene sentido por sí solo |
| `aside` | Horarios de secretaría, descuentos, "¿Cómo sigue?" | Información complementaria |
| `footer` | Pie con teléfono | Es el pie de página |

### `estilos.css`

Está ordenado por tipo de selector, para poder mostrarlos rápido:

- **De tipo:** `body`, `h1`, `a`, `img` (eligen por etiqueta).
- **De clase:** `.tarjeta`, `.boton`, `.recuadro` (se repiten en muchos elementos).
- **De ID:** `#lista-actividades` (el único contenedor donde JavaScript dibuja las tarjetas).
- **De atributo:** `.menu a[aria-current="page"]` marca en el menú la página actual; `input[aria-invalid="true"]` pinta de rojo los campos con error.
- **Media queries:** hasta 1024 px muestra 2 tarjetas por fila; hasta 768 px (celular), una sola columna.

### `script.js`: el recorrido que te van a pedir

**"¿Qué pasa cuando el usuario cambia el filtro?"**

1. Al cargar la página, el navegador termina de armar el DOM y dispara `DOMContentLoaded`.
2. `iniciarCatalogo()` registra con `addEventListener` la función `filtrarActividades` en el evento `change` del select y en el `input` del campo edad.
3. El usuario elige "Fútbol". El navegador dispara `change` y ejecuta `filtrarActividades`.
4. La función lee los dos filtros y usa `actividades.filter(...)`, que llama al método `esAptaParaEdad(edad)` de cada objeto.
5. Le pasa el resultado a `dibujarCatalogo(lista)`, que vacía `#lista-actividades` y agrega un `article` por actividad con `appendChild`.
6. Como cambió el DOM, el navegador redibuja la pantalla. La página nunca se recarga.

**"¿Qué es el DOM?"** El árbol de objetos que arma el navegador a partir del HTML. JavaScript no toca el archivo, modifica ese árbol con `getElementById`, `createElement`, `appendChild`, `textContent`.

**La validación:** `evento.preventDefault()` frena el envío del formulario; `validarFormulario()` revisa campo por campo y escribe cada error en el `<span>` que está al lado del campo. El DNI se valida con `/^\d{7,8}$/` (RN7). El atributo `novalidate` del `<form>` apaga la validación automática del navegador para que se vean nuestros mensajes.

**Objetos del navegador:** el botón "Volver" usa `history.back()`; si no hay historial, usa `location.href` para ir al inicio. Además, `location.search` lee el `?actividad=4` de la URL para dejar elegida la actividad cuando se llega desde una tarjeta.

---

## Parte B: sistema de secretaría

### Las capas y el recorrido de "Marta guarda una actividad"

```
Navegador ──POST /actividades/guardar──> ActividadController   (controller)
                                              │ actividadService.guardar(actividad)
                                              v
                                         ActividadService       (service: valida reglas)
                                              │ actividadRepository.save(...)
                                              v
                                         ActividadRepository    (repository: JPA arma el SQL)
                                              │ INSERT / UPDATE
                                              v
                                           MySQL
```

La respuesta vuelve hacia arriba: el controlador hace `redirect:/actividades`, se vuelve a pedir el listado y Thymeleaf dibuja la tabla con el mensaje "Actividad creada correctamente".

### Archivo por archivo

**`SocioclubApplication.java`**: el `main`. `@SpringBootApplication` hace que Spring busque todas las clases anotadas del paquete y cree los beans.

**`model/Actividad.java`, `Socio.java`, `Inscripcion.java`**: las entidades. `@Entity` = tabla, atributo = columna, objeto = fila. `@Id` + `@GeneratedValue` = clave primaria autoincremental. Getters y setters escritos a mano (sin Lombok).

- En `Inscripcion`: `@ManyToOne` + `@JoinColumn(name = "socio_id")` crea la clave foránea.
- En `Socio` y `Actividad`: `@OneToMany(mappedBy = ...)` dice "la clave foránea la maneja el otro lado; acá no crees columna".
- En `Actividad`: `@Transient cupoDisponible` es un dato calculado que no se guarda en la base.

**Cómo queda en tablas:** `socio`, `actividad` e `inscripcion`. La tabla `inscripcion` tiene `socio_id` y `actividad_id`: es la tabla intermedia de una relación muchos a muchos, con datos propios (fecha y estado).

**`repository/*.java`**: interfaces que extienden `JpaRepository`, con lo que ya traen `save`, `findById`, `findAll`, `delete`.

- Métodos derivados del nombre: `findAllByOrderByNombreAsc`, `findByDisciplinaOrderByNombreAsc`, `findByApellidoContainingIgnoreCase`. Spring lee el nombre como una frase y arma el SQL.
- `@Query` en `InscripcionRepository.contarPorActividadYEstado`: consulta JPQL escrita a mano (usa nombres de clases y atributos, no de tablas) que cuenta las inscripciones activas de una actividad.

**`service/ActividadService.java`**: las reglas. Calcula el cupo disponible, valida los datos (edad mínima ≤ máxima, cupo > 0, cuota ≥ 0), no deja bajar el cupo por debajo de los inscriptos activos y no deja eliminar una actividad con inscriptos activos. Si algo no se cumple, lanza `ReglaNegocioException`.

**Por qué las reglas están acá y no en el controlador:** porque en la semana 3 la API REST va a usar este mismo servicio. Si las reglas estuvieran en el controlador de Thymeleaf, habría que copiarlas en el controlador REST. En el servicio se escriben una vez y valen para las dos puertas de entrada.

**`controller/ActividadController.java`**: recibe los pedidos, llama al servicio y elige la vista. Atrapa la `ReglaNegocioException` y pone el mensaje en el modelo para que lo muestre la vista. Nunca usa el repositorio directamente.

**Inyección de dependencias:** el controlador recibe el servicio en su constructor; nunca hace `new ActividadService()`. Spring crea el bean y se lo pasa.

**`templates/fragmentos.html`**: cabecera, encabezado y pie comunes, que las otras vistas incluyen con `th:replace` (como el `include` de PHP).

**`templates/actividades/lista.html`**: `th:each` recorre las actividades (como un `foreach`), `th:text` muestra datos, `th:if` muestra los mensajes solo si existen, `th:classappend` pinta en rojo el cupo agotado.

**`templates/actividades/formulario.html`**: `th:object="${actividad}"` + `th:field="*{nombre}"` conectan cada campo con el objeto: al editar lo completa, al enviar lo carga. El `id` viaja oculto: vacío = alta, con valor = modificación.

**`application.properties`**: la conexión a MySQL; `ddl-auto=create` hace que Hibernate cree las tablas desde las entidades; `show-sql=true` muestra en la consola el SQL que genera (ideal para mostrar el ORM en la defensa).

**`data.sql`**: las 8 actividades, 20 socios y 20 inscripciones. Se ejecuta solo al arrancar.

---

## Cambios en vivo más probables

| Si el docente pide... | Dónde se toca |
|---|---|
| Agregar un dato a la tarjeta del sitio público | `script.js`: atributo en la clase `Actividad`, valor en el arreglo y una línea en `crearTarjeta` |
| Cambiar el color de las tarjetas | `estilos.css`, clase `.tarjeta` o `.tarjeta-horario` |
| Que en celular se vean 2 columnas | `estilos.css`, media query de 768 px: `repeat(2, 1fr)` |
| Agregar una columna a la tabla de secretaría | `lista.html`: un `<th>` y un `<td th:text="${a.dato}">` |
| Agregar un campo a la actividad | Atributo + getter/setter en `Actividad.java`, campo en `formulario.html`, columna en `lista.html` |
| Cambiar el mensaje al eliminar con inscriptos | `ActividadService.eliminar` |

## Para la bitácora de IA (Anexo D)

Una entrada lista para usar: _"Le pedimos a la IA el frontend del sistema. Nos devolvió un proyecto en Next.js con una API temporal en JavaScript. Detectamos que no cumplía con las tecnologías de la materia (la Parte A pide HTML/CSS/JS puro y la Parte B, Spring Boot con Thymeleaf), así que rehicimos el sitio público en HTML puro y armamos el backend en Spring Boot. Reutilizamos el script SQL, los textos y el diseño; los componentes React quedan como base para la Parte D con create-react-app, React Router y Axios."_
