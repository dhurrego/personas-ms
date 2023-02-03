package co.com.sofka.model.estadisticas.dto;

import co.com.sofka.model.estadisticas.enums.TipoMovimiento;
import lombok.Builder;

@Builder
public record HistorialCambioEstadoDTO(
        String dniSofkiano,
        String nombreCompletoSofkiano,
        TipoMovimiento tipoMovimiento
) {
}
