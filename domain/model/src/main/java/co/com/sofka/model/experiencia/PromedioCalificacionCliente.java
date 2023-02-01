package co.com.sofka.model.experiencia;

import lombok.Builder;

@Builder
public record PromedioCalificacionCliente(
        String nitCliente,
        Double promedio
) {
}
