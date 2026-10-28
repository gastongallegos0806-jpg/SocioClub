/* =========================================================
   script.js: JavaScript del sitio público del Club Los Algarrobos

   Partes:
   1. Clase Actividad y arreglo de actividades (los datos)
   2. Catálogo y filtros (actividades.html)
   3. Validación del formulario (preinscripcion.html)
   4. Botón "Volver" (objeto history del navegador)
   5. Arranque: se ejecuta cuando el DOM está listo
   ========================================================= */


/* ---------------------------------------------------------
   1. CLASE ACTIVIDAD Y DATOS
   --------------------------------------------------------- */

// Una clase es un "molde" para crear objetos con los mismos datos y métodos.
class Actividad {
  constructor(id, nombre, disciplina, dias, horario, edadMinima, edadMaxima, cupo, cuotaMensual, profesor) {
    this.id = id;
    this.nombre = nombre;
    this.disciplina = disciplina;
    this.dias = dias;
    this.horario = horario;
    this.edadMinima = edadMinima;
    this.edadMaxima = edadMaxima;
    this.cupo = cupo;
    this.cuotaMensual = cuotaMensual;
    this.profesor = profesor;
  }

  // Método: devuelve true si la edad está dentro del rango de la actividad.
  esAptaParaEdad(edad) {
    return edad >= this.edadMinima && edad <= this.edadMaxima;
  }

  // Método: devuelve la cuota con formato argentino, por ejemplo "$ 18.000".
  cuotaFormateada() {
    return "$ " + this.cuotaMensual.toLocaleString("es-AR");
  }
}

// Arreglo de objetos: las 8 actividades de la sección 3.3 del enunciado.
const actividades = [
  new Actividad(1, "Fútbol infantil", "Fútbol", "Lunes y miércoles", "18:00", 6, 12, 40, 18000, "Diego Ferreyra"),
  new Actividad(2, "Vóley mixto", "Vóley", "Martes y jueves", "19:00", 13, 60, 24, 20000, "Laura Ceballos"),
  new Actividad(3, "Básquet juvenil", "Básquet", "Lunes y viernes", "19:30", 12, 17, 20, 19000, "Martín Oviedo"),
  new Actividad(4, "Natación", "Natación", "Martes y jueves", "17:00", 5, 70, 30, 26000, "Sofía Bustos"),
  new Actividad(5, "Patín artístico", "Patín", "Miércoles y viernes", "17:30", 5, 15, 25, 21000, "Carla Peralta"),
  new Actividad(6, "Ajedrez", "Ajedrez", "Sábados", "10:00", 8, 99, 16, 9000, "Raúl Giménez"),
  new Actividad(7, "Funcional adultos", "Fitness", "Lunes, miércoles y viernes", "20:30", 18, 65, 30, 22000, "Paula Luna"),
  new Actividad(8, "Yoga", "Bienestar", "Martes y jueves", "9:00", 16, 99, 15, 17000, "Inés Arguello")
];


/* ---------------------------------------------------------
   2. CATÁLOGO Y FILTROS (solo se usa en actividades.html)
   --------------------------------------------------------- */

// Crea en memoria el elemento <article> de una tarjeta y lo devuelve.
function crearTarjeta(actividad) {
  const tarjeta = document.createElement("article");
  tarjeta.className = "tarjeta";

  tarjeta.innerHTML = `
    <div class="tarjeta-horario">
      <span class="tarjeta-hora">${actividad.horario}</span>
      <span class="tarjeta-dias">${actividad.dias}</span>
    </div>
    <div class="tarjeta-cuerpo">
      <h3>${actividad.nombre}</h3>
      <span class="tarjeta-disciplina">${actividad.disciplina}</span>
      <ul class="tarjeta-datos">
        <li>Edades: de ${actividad.edadMinima} a ${actividad.edadMaxima} años</li>
        <li>Profesor/a: ${actividad.profesor}</li>
      </ul>
    </div>
    <div class="tarjeta-pie">
      <span class="tarjeta-cuota">${actividad.cuotaFormateada()} <small>por mes</small></span>
      <a href="preinscripcion.html?actividad=${actividad.id}">Preinscribirme</a>
    </div>
  `;
  return tarjeta;
}

// Borra lo que hay en el contenedor y dibuja una tarjeta por cada actividad de la lista.
function dibujarCatalogo(lista) {
  const contenedor = document.getElementById("lista-actividades");
  const contador = document.getElementById("contador-resultados");

  contenedor.innerHTML = ""; // vaciamos la lista anterior

  if (lista.length === 0) {
    contenedor.innerHTML = `
      <p class="sin-resultados">
        No hay actividades para esa combinación. Probá con otra edad o con otra disciplina.
      </p>`;
  } else {
    lista.forEach(function (actividad) {
      contenedor.appendChild(crearTarjeta(actividad)); // agregamos la tarjeta al DOM
    });
  }

  contador.textContent = "Mostrando " + lista.length + " de " + actividades.length + " actividades";
}

// Lee los filtros, filtra el arreglo y vuelve a dibujar el catálogo.
// Esta función se ejecuta cada vez que el usuario cambia un filtro.
function filtrarActividades() {
  const disciplina = document.getElementById("filtro-disciplina").value;
  const textoEdad = document.getElementById("filtro-edad").value;
  const edad = Number(textoEdad);

  const resultado = actividades.filter(function (actividad) {
    const coincideDisciplina = disciplina === "todas" || actividad.disciplina === disciplina;
    const coincideEdad = textoEdad === "" || actividad.esAptaParaEdad(edad);
    return coincideDisciplina && coincideEdad;
  });

  dibujarCatalogo(resultado);
}

// Carga las disciplinas en el <select>, registra los eventos y dibuja todo la primera vez.
function iniciarCatalogo() {
  const contenedor = document.getElementById("lista-actividades");
  if (!contenedor) return; // si no estamos en actividades.html, no hacemos nada

  const selectDisciplina = document.getElementById("filtro-disciplina");
  const inputEdad = document.getElementById("filtro-edad");
  const botonLimpiar = document.getElementById("boton-limpiar");

  // Opciones del select a partir de los datos (sin repetir disciplinas)
  const disciplinas = [];
  actividades.forEach(function (actividad) {
    if (!disciplinas.includes(actividad.disciplina)) {
      disciplinas.push(actividad.disciplina);
    }
  });
  disciplinas.sort().forEach(function (disciplina) {
    const opcion = document.createElement("option");
    opcion.value = disciplina;
    opcion.textContent = disciplina;
    selectDisciplina.appendChild(opcion);
  });

  // Eventos: cuando cambia el select o se escribe una edad, se vuelve a filtrar
  selectDisciplina.addEventListener("change", filtrarActividades);
  inputEdad.addEventListener("input", filtrarActividades);

  botonLimpiar.addEventListener("click", function () {
    selectDisciplina.value = "todas";
    inputEdad.value = "";
    filtrarActividades();
  });

  dibujarCatalogo(actividades); // primera vez: todas las actividades
}


/* ---------------------------------------------------------
   3. VALIDACIÓN DEL FORMULARIO (solo en preinscripcion.html)
   --------------------------------------------------------- */

// RN7: el DNI debe tener 7 u 8 dígitos numéricos, sin puntos.
// ^ = principio, \d = un dígito, {7,8} = entre 7 y 8 veces, $ = final.
function esDniValido(dni) {
  return /^\d{7,8}$/.test(dni);
}

// Email: algo + @ + algo + . + algo, sin espacios.
function esEmailValido(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

// Escribe el mensaje de error en el <span> que está junto al campo.
function mostrarError(campo, mensaje) {
  document.getElementById("error-" + campo.id).textContent = mensaje;
  campo.setAttribute("aria-invalid", "true"); // el CSS lo pinta de rojo
}

// Borra el error de un campo.
function limpiarError(campo) {
  document.getElementById("error-" + campo.id).textContent = "";
  campo.removeAttribute("aria-invalid");
}

// Revisa todos los campos. Devuelve true si el formulario es válido.
function validarFormulario() {
  const nombre = document.getElementById("nombre");
  const apellido = document.getElementById("apellido");
  const dni = document.getElementById("dni");
  const email = document.getElementById("email");
  const actividad = document.getElementById("actividad");
  let esValido = true;

  [nombre, apellido, dni, email, actividad].forEach(limpiarError);

  if (nombre.value.trim() === "") {
    mostrarError(nombre, "Escribí tu nombre.");
    esValido = false;
  }

  if (apellido.value.trim() === "") {
    mostrarError(apellido, "Escribí tu apellido.");
    esValido = false;
  }

  if (dni.value.trim() === "") {
    mostrarError(dni, "Escribí tu DNI.");
    esValido = false;
  } else if (!esDniValido(dni.value.trim())) {
    mostrarError(dni, "El DNI debe tener 7 u 8 números, sin puntos.");
    esValido = false;
  }

  if (email.value.trim() === "") {
    mostrarError(email, "Escribí tu email.");
    esValido = false;
  } else if (!esEmailValido(email.value.trim())) {
    mostrarError(email, "Revisá el email: debe tener el formato nombre@dominio.com.");
    esValido = false;
  }

  if (actividad.value === "") {
    mostrarError(actividad, "Elegí una actividad.");
    esValido = false;
  }

  return esValido;
}

function iniciarFormulario() {
  const formulario = document.getElementById("form-preinscripcion");
  if (!formulario) return; // si no estamos en preinscripcion.html, no hacemos nada

  const selectActividad = document.getElementById("actividad");
  const mensajeExito = document.getElementById("mensaje-exito");

  // Opciones del select desde el mismo arreglo del catálogo
  actividades.forEach(function (actividad) {
    const opcion = document.createElement("option");
    opcion.value = actividad.id;
    opcion.textContent = actividad.nombre + " (" + actividad.dias + " " + actividad.horario + ")";
    selectActividad.appendChild(opcion);
  });

  // Objeto location: si se llega desde el catálogo con ?actividad=4,
  // dejamos esa actividad elegida en el select.
  const parametros = new URLSearchParams(window.location.search);
  const idElegido = parametros.get("actividad");
  if (idElegido) {
    selectActividad.value = idElegido;
  }

  // Evento submit: se dispara al apretar "Enviar preinscripción"
  formulario.addEventListener("submit", function (evento) {
    evento.preventDefault(); // frenamos el envío para poder validar primero
    mensajeExito.hidden = true;

    if (validarFormulario()) {
      const nombre = document.getElementById("nombre").value.trim();
      mensajeExito.textContent = "¡Listo, " + nombre + "! Recibimos tu preinscripción. " +
        "La secretaría se va a comunicar con vos en los próximos días.";
      mensajeExito.hidden = false;
      formulario.reset();
    }
  });

  // Mientras la persona corrige un campo, le borramos el error de ese campo
  formulario.addEventListener("input", function (evento) {
    if (evento.target.id && document.getElementById("error-" + evento.target.id)) {
      limpiarError(evento.target);
    }
  });
}


/* ---------------------------------------------------------
   4. BOTÓN "VOLVER" (objeto history del navegador)
   --------------------------------------------------------- */

function iniciarBotonesVolver() {
  const botones = document.querySelectorAll(".boton-volver");

  botones.forEach(function (boton) {
    boton.addEventListener("click", function () {
      // history.back() vuelve a la página anterior del historial.
      // Si la persona entró directo (sin historial), la mandamos al inicio.
      if (window.history.length > 1) {
        window.history.back();
      } else {
        window.location.href = "index.html";
      }
    });
  });
}


/* ---------------------------------------------------------
   5. ARRANQUE
   --------------------------------------------------------- */

// DOMContentLoaded: el navegador avisa cuando terminó de armar el DOM.
// Recién ahí buscamos elementos, porque antes todavía no existen.
document.addEventListener("DOMContentLoaded", function () {
  iniciarCatalogo();
  iniciarFormulario();
  iniciarBotonesVolver();
});
