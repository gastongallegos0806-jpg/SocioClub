-- =========================================================
-- SocioClub: script de creación de la base y datos iniciales
-- (entregable "Base de datos .sql" de la sección 8 del enunciado)
--
-- Nota: el backend NO necesita este archivo para funcionar, porque
-- Hibernate crea las tablas solo (ddl-auto=create) y carga data.sql.
-- Este script sirve para crear la base a mano desde MySQL Workbench.
-- =========================================================
CREATE DATABASE IF NOT EXISTS socioclub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE socioclub;

DROP TABLE IF EXISTS inscripcion;
DROP TABLE IF EXISTS actividad;
DROP TABLE IF EXISTS socio;

CREATE TABLE socio (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  dni VARCHAR(8) NOT NULL UNIQUE,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  fecha_nacimiento DATE NOT NULL,
  email VARCHAR(180) NOT NULL,
  telefono VARCHAR(30),
  estado_cuota ENUM('AL_DIA','MOROSO') NOT NULL DEFAULT 'AL_DIA'
);

CREATE TABLE actividad (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  disciplina VARCHAR(80) NOT NULL,
  profesor VARCHAR(120) NOT NULL,
  dias VARCHAR(120) NOT NULL,
  horario VARCHAR(20) NOT NULL,
  edad_minima INT NOT NULL,
  edad_maxima INT NOT NULL,
  cupo_maximo INT NOT NULL,
  cuota_mensual DECIMAL(12,2) NOT NULL
);

CREATE TABLE inscripcion (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  socio_id BIGINT NOT NULL,
  actividad_id BIGINT NOT NULL,
  fecha_inscripcion DATE NOT NULL,
  estado ENUM('ACTIVA','BAJA') NOT NULL DEFAULT 'ACTIVA',
  FOREIGN KEY (socio_id) REFERENCES socio(id),
  FOREIGN KEY (actividad_id) REFERENCES actividad(id)
);

-- Datos iniciales: se ejecuta automáticamente al arrancar (después de que Hibernate crea las tablas).
-- Actividades de la sección 3.3 del enunciado.
INSERT INTO actividad (id, nombre, disciplina, profesor, dias, horario, edad_minima, edad_maxima, cupo_maximo, cuota_mensual) VALUES
  (1, 'Fútbol infantil', 'Fútbol', 'Diego Ferreyra', 'Lunes y miércoles', '18:00', 6, 12, 40, 18000),
  (2, 'Vóley mixto', 'Vóley', 'Laura Ceballos', 'Martes y jueves', '19:00', 13, 60, 24, 20000),
  (3, 'Básquet juvenil', 'Básquet', 'Martín Oviedo', 'Lunes y viernes', '19:30', 12, 17, 20, 19000),
  (4, 'Natación', 'Natación', 'Sofía Bustos', 'Martes y jueves', '17:00', 5, 70, 30, 26000),
  (5, 'Patín artístico', 'Patín', 'Carla Peralta', 'Miércoles y viernes', '17:30', 5, 15, 25, 21000),
  (6, 'Ajedrez', 'Ajedrez', 'Raúl Giménez', 'Sábados', '10:00', 8, 99, 16, 9000),
  (7, 'Funcional adultos', 'Fitness', 'Paula Luna', 'Lunes, miércoles y viernes', '20:30', 18, 65, 30, 22000),
  (8, 'Yoga', 'Bienestar', 'Inés Arguello', 'Martes y jueves', '09:00', 16, 99, 15, 17000);

-- 20 socios de prueba con edades variadas. María López (3) y Bruno Suárez (6) son MOROSOS.
INSERT INTO socio (id, dni, nombre, apellido, fecha_nacimiento, email, telefono, estado_cuota) VALUES
  (1, '40123456', 'Lucía', 'Pérez', '2000-04-10', 'lucia@ejemplo.com', '3515550101', 'AL_DIA'),
  (2, '45123456', 'Tomás', 'Gómez', '2015-03-20', 'tomas@ejemplo.com', '3515550102', 'AL_DIA'),
  (3, '38123456', 'María', 'López', '1994-07-18', 'maria@ejemplo.com', '3515550103', 'MOROSO'),
  (4, '42123456', 'Pedro', 'Díaz', '1998-11-03', 'pedro@ejemplo.com', '3515550104', 'AL_DIA'),
  (5, '47123456', 'Ana', 'Ruiz', '2015-09-11', 'ana@ejemplo.com', '3515550105', 'AL_DIA'),
  (6, '33123456', 'Bruno', 'Suárez', '1988-05-22', 'bruno@ejemplo.com', '3515550106', 'MOROSO'),
  (7, '44123456', 'Carla', 'Méndez', '2004-01-08', 'carla@ejemplo.com', '3515550107', 'AL_DIA'),
  (8, '29123456', 'Daniel', 'Ortiz', '1982-08-13', 'daniel@ejemplo.com', '3515550108', 'AL_DIA'),
  (9, '46123456', 'Elena', 'Castro', '2013-06-19', 'elena@ejemplo.com', '3515550109', 'AL_DIA'),
  (10, '35123456', 'Felipe', 'Ramos', '1990-02-04', 'felipe@ejemplo.com', '3515550110', 'AL_DIA'),
  (11, '43123456', 'Gabriela', 'Núñez', '2002-10-29', 'gabriela@ejemplo.com', '3515550111', 'AL_DIA'),
  (12, '30123456', 'Hugo', 'Silva', '1985-12-16', 'hugo@ejemplo.com', '3515550112', 'AL_DIA'),
  (13, '48123456', 'Irene', 'Morales', '2016-08-01', 'irene@ejemplo.com', '3515550113', 'AL_DIA'),
  (14, '39123456', 'Julián', 'Herrera', '1996-07-15', 'julian@ejemplo.com', '3515550114', 'AL_DIA'),
  (15, '37123456', 'Karen', 'Vega', '1992-09-06', 'karen@ejemplo.com', '3515550115', 'AL_DIA'),
  (16, '32111221', 'Rocío', 'Aguirre', '1993-01-16', 'socio16@ejemplo.com', '3515550016', 'AL_DIA'),
  (17, '32111222', 'Matías', 'Acosta', '1989-12-04', 'socio17@ejemplo.com', '3515550017', 'AL_DIA'),
  (18, '32111223', 'Sofía', 'Ledesma', '2001-09-07', 'socio18@ejemplo.com', '3515550018', 'AL_DIA'),
  (19, '32111224', 'Iván', 'Paredes', '1997-05-03', 'socio19@ejemplo.com', '3515550019', 'AL_DIA'),
  (20, '32111225', 'Paula', 'Benítez', '2003-06-21', 'socio20@ejemplo.com', '3515550020', 'AL_DIA');

-- Inscripciones: Yoga arranca con 14 de 15 lugares ocupados (cupo casi completo, RN1).
-- Lucía Pérez (1) tiene 2 actividades activas y Pedro Díaz (4) tiene 3 (sirve para RN5).
INSERT INTO inscripcion (id, socio_id, actividad_id, estado, fecha_inscripcion) VALUES
  (1, 1, 8, 'ACTIVA', '2026-10-01'),
  (2, 1, 7, 'ACTIVA', '2026-10-03'),
  (3, 2, 1, 'ACTIVA', '2026-10-05'),
  (4, 4, 2, 'ACTIVA', '2026-10-07'),
  (5, 5, 5, 'ACTIVA', '2026-10-08'),
  (6, 7, 6, 'ACTIVA', '2026-10-09'),
  (7, 4, 8, 'ACTIVA', '2026-10-10'),
  (8, 7, 8, 'ACTIVA', '2026-10-10'),
  (9, 8, 8, 'ACTIVA', '2026-10-10'),
  (10, 10, 8, 'ACTIVA', '2026-10-10'),
  (11, 11, 8, 'ACTIVA', '2026-10-10'),
  (12, 12, 8, 'ACTIVA', '2026-10-10'),
  (13, 14, 8, 'ACTIVA', '2026-10-10'),
  (14, 15, 8, 'ACTIVA', '2026-10-10'),
  (15, 16, 8, 'ACTIVA', '2026-10-10'),
  (16, 17, 8, 'ACTIVA', '2026-10-10'),
  (17, 18, 8, 'ACTIVA', '2026-10-10'),
  (18, 19, 8, 'ACTIVA', '2026-10-10'),
  (19, 20, 8, 'ACTIVA', '2026-10-10'),
  (20, 4, 7, 'ACTIVA', '2026-10-11');
