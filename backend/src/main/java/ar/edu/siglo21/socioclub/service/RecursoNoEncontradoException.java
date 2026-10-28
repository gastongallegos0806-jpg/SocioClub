package ar.edu.siglo21.socioclub.service;

// Se lanza cuando se busca algo que no existe (por ejemplo una actividad con un id inválido).
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
