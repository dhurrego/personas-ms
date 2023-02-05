package co.com.sofka.broker.estadisticas.historialcambioestado.mapper;

import co.com.sofka.broker.estadisticas.commons.dto.SofkianoHistorialDTO;
import co.com.sofka.broker.estadisticas.commons.mapper.SofkianoMapper;
import co.com.sofka.broker.estadisticas.historialcambioestado.dto.AgregarHistorialCambioEstadoDTO;
import co.com.sofka.model.estadisticas.dto.HistorialCambioEstadoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HistorialCambioEstadoMapper {

    public static AgregarHistorialCambioEstadoDTO crearAgregarHistorial(
            HistorialCambioEstadoDTO historialCambioEstado) {

        SofkianoHistorialDTO sofkianoHistorialDTO = SofkianoMapper.crearSofkianoDTO(historialCambioEstado.dniSofkiano(),
                historialCambioEstado.nombreCompletoSofkiano());

        return new AgregarHistorialCambioEstadoDTO(sofkianoHistorialDTO, historialCambioEstado.tipoMovimiento());
    }
}
