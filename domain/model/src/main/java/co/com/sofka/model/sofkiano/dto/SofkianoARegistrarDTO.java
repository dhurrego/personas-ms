package co.com.sofka.model.sofkiano.dto;

import java.io.Serializable;
import java.util.Optional;

public record SofkianoARegistrarDTO(
        String tipoIdentificacion,
        String numeroIdentificacion,
        String primerNombre,
        Optional<String> segundoNombre,
        String primerApellido,
        Optional<String> segundoApellido,
        String direccion
) implements Serializable {
}
