package co.com.sofka.model.sofkiano.factoria;

import co.com.sofka.model.sofkiano.ConsolidadoAsignacionSofkiano;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.dto.ConsolidadoAsignacionSofkianoDTO;
import co.com.sofka.model.sofkiano.dto.SofkianoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SofkianoDTOsFactory {
    public static SofkianoDTO crearSofkianoDTO(Sofkiano sofkiano) {
        return SofkianoDTO
                .builder()
                .tipoIdentificacion(sofkiano.getTipoIdentificacion().name())
                .numeroIdentificacion(sofkiano.getNumeroIdentificacion())
                .primerNombre(sofkiano.getPrimerNombre())
                .segundoNombre(sofkiano.getSegundoNombre())
                .primerApellido(sofkiano.getPrimerApellido())
                .segundoApellido(sofkiano.getSegundoApellido())
                .direccion(sofkiano.getDireccion())
                .cliente(sofkiano.getCliente())
                .activo(sofkiano.isActivo())
                .build();
    }

    public static ConsolidadoAsignacionSofkianoDTO crearConsolidadoAsignacionDTO(
            ConsolidadoAsignacionSofkiano consolidadoAsignacionSofkiano) {

        return new ConsolidadoAsignacionSofkianoDTO(
                consolidadoAsignacionSofkiano.conAsignacion(),
                consolidadoAsignacionSofkiano.sinAsignacion(),
                consolidadoAsignacionSofkiano.totalSofkianos()
        );
    }
}
