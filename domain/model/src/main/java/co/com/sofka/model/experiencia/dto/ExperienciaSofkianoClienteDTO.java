package co.com.sofka.model.experiencia.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExperienciaSofkianoClienteDTO(
        String dniSofkiano,
        String nitCliente,
        Integer nivelSatisfaccion,
        String descripcion,
        LocalDateTime fechaCreacion
) {
}
