package co.com.sofka.model.sofkiano.factoria;

import co.com.sofka.model.sofkiano.ConsolidadoAsignacionSofkiano;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.dto.ConsolidadoAsignacionSofkianoDTO;
import co.com.sofka.model.sofkiano.dto.SofkianoDTO;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SofkianoDTOsFactoryTest {

    private static final String TIPO_IDENTIFICACION = "CC";
    private static final String NUMERO_IDENTIFICACION = "1234567890";
    private static final String STRING_TEST = "TEST";

    @Test
    void crearSofkianoDTO() {
        Sofkiano sofkiano = Sofkiano.builder()
                .dni(TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION))
                .tipoIdentificacion(TipoIdentificacion.valueOf(TIPO_IDENTIFICACION))
                .numeroIdentificacion(NUMERO_IDENTIFICACION)
                .primerNombre(STRING_TEST)
                .segundoNombre(Optional.of(STRING_TEST))
                .primerApellido(STRING_TEST)
                .segundoApellido(Optional.of(STRING_TEST))
                .activo(true)
                .direccion(STRING_TEST)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .fechaSalida(Optional.empty())
                .cliente(Optional.empty())
                .build();

        SofkianoDTO sofkianoDTO = SofkianoDTOsFactory.crearSofkianoDTO(sofkiano);

        assertEquals(TIPO_IDENTIFICACION, sofkianoDTO.tipoIdentificacion());
        assertEquals(NUMERO_IDENTIFICACION, sofkianoDTO.numeroIdentificacion());
        assertEquals(STRING_TEST, sofkianoDTO.primerNombre());
        assertEquals(STRING_TEST, sofkianoDTO.primerApellido());
        assertEquals(STRING_TEST, sofkianoDTO.direccion());
        assertTrue(sofkianoDTO.activo());
    }

    @Test
    void crearConsolidadoAsignacionDTO() {
        ConsolidadoAsignacionSofkiano consolidado = new ConsolidadoAsignacionSofkiano(0, 0, 0);

        ConsolidadoAsignacionSofkianoDTO consolidadoDTO = SofkianoDTOsFactory.crearConsolidadoAsignacionDTO(consolidado);

        assertEquals(0, consolidadoDTO.conAsignacion());
        assertEquals(0, consolidadoDTO.sinAsignacion());
        assertEquals(0, consolidadoDTO.totalSofkianos());
    }
}