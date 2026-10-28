package ar.edu.siglo21.socioclub.service;

// Excepción propia: el servicio la "lanza" cuando una operación no cumple
// una regla de negocio. El controlador la atrapa y muestra el mensaje.
public class ReglaNegocioException extends RuntimeException {

    // Código de la regla (por ejemplo "RN1"); la API REST lo va a devolver en el JSON.
    private final String regla;

    public ReglaNegocioException(String regla, String mensaje) {
        super(mensaje);
        this.regla = regla;
    }

    public String getRegla() {
        return regla;
    }
}
