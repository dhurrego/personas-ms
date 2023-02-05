package co.com.sofka.broker.estadisticas.historialasignacionsofkiano.mapper;

import co.com.sofka.broker.estadisticas.commons.dto.ClienteHistorialDTO;
import co.com.sofka.broker.estadisticas.commons.dto.SofkianoHistorialDTO;
import co.com.sofka.broker.estadisticas.commons.mapper.ClienteMapper;
import co.com.sofka.broker.estadisticas.commons.mapper.SofkianoMapper;
import co.com.sofka.broker.estadisticas.historialasignacionsofkiano.dto.AgregarHistorialAsignacionSofkianoDTO;
import co.com.sofka.model.estadisticas.dto.HistorialAsignacionSofkianoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HistorialAsignacionMapper {

    public static AgregarHistorialAsignacionSofkianoDTO crearAgregarHistorial(
            HistorialAsignacionSofkianoDTO historialAsignacion) {

        SofkianoHistorialDTO sofkianoHistorialDTO = SofkianoMapper.crearSofkianoDTO(historialAsignacion.dniSofkiano(),
                historialAsignacion.nombreCompletoSofkiano());

        ClienteHistorialDTO clienteHistorialDTO = ClienteMapper.crearClienteDTO(historialAsignacion.nitCliente(),
                historialAsignacion.razonSocialCliente());

        return new AgregarHistorialAsignacionSofkianoDTO(sofkianoHistorialDTO, clienteHistorialDTO,
                historialAsignacion.tipoMovimiento());
    }
}
