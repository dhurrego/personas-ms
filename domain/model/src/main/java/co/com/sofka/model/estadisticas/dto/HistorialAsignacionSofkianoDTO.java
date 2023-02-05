package co.com.sofka.model.estadisticas.dto;

import co.com.sofka.model.estadisticas.enums.TipoMovimiento;
import lombok.Builder;

@Builder
public record HistorialAsignacionSofkianoDTO(
        String dniSofkiano,
        String nombreCompletoSofkiano,
        String nitCliente,
        String razonSocialCliente,
        TipoMovimiento tipoMovimiento
) {
}
