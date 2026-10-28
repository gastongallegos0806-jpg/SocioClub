package ar.edu.siglo21.socioclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Punto de entrada. @SpringBootApplication le dice a Spring que busque
// todas las clases anotadas (@Controller, @Service, @Repository, @Entity)
// dentro de este paquete y sus subpaquetes, y las cree como beans.
@SpringBootApplication
public class SocioclubApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocioclubApplication.class, args);
    }
}
