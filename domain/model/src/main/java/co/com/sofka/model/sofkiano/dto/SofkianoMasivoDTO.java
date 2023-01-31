package co.com.sofka.model.sofkiano.dto;

import lombok.Builder;

import java.util.Optional;

@Builder()
public record SofkianoMasivoDTO(
        Integer numeroFila,
        String tipoIdentificacion,
        String numeroIdentificacion,
        String primerNombre,
        Optional<String> segundoNombre,
        String primerApellido,
        Optional<String> segundoApellido,
        String direccion,
        boolean activo,
        Optional<String> nitCliente
) {
}
