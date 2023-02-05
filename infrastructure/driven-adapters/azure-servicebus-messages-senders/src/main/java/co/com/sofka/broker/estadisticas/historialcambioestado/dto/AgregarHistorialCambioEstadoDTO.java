package co.com.sofka.broker.estadisticas.historialcambioestado.dto;

import co.com.sofka.broker.estadisticas.commons.dto.SofkianoHistorialDTO;
import co.com.sofka.model.estadisticas.enums.TipoMovimiento;

import java.io.Serializable;

public record AgregarHistorialCambioEstadoDTO(
        SofkianoHistorialDTO sofkiano,
        TipoMovimiento tipoMovimiento
) implements Serializable {
}