package co.com.sofka.model.experiencia.dto;

import lombok.Builder;

@Builder
public record PromedioCalificacionClienteDTO(
        String nitCliente,
        Double promedio
) {
}
