package co.com.sofka.broker.estadisticas.historialasignacionsofkiano.dto;

import co.com.sofka.broker.estadisticas.commons.dto.ClienteHistorialDTO;
import co.com.sofka.broker.estadisticas.commons.dto.SofkianoHistorialDTO;
import co.com.sofka.model.estadisticas.enums.TipoMovimiento;

import java.io.Serializable;

public record AgregarHistorialAsignacionSofkianoDTO(
        SofkianoHistorialDTO sofkiano,
        ClienteHistorialDTO cliente,
        TipoMovimiento tipoMovimiento
) implements Serializable {
}