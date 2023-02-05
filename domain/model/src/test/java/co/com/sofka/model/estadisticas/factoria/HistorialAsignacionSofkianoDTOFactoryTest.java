package co.com.sofka.model.estadisticas.factoria;

import co.com.sofka.model.estadisticas.dto.HistorialAsignacionSofkianoDTO;
import co.com.sofka.model.estadisticas.enums.TipoMovimiento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistorialAsignacionSofkianoDTOFactoryTest {

    @Test
    void crearAgregarHistorialAsignacionDTO() {
        final String dniSofkiano = "CC123133";
        final String stringTest = "TEST";
        final String nit = "89050622";

        HistorialAsignacionSofkianoDTO historialAsignacionSofkianoDTO = HistorialAsignacionSofkianoDTOFactory
                .crearAgregarHistorialAsignacionDTO(dniSofkiano, stringTest, nit, stringTest, TipoMovimiento.SALIDA);

        assertEquals(dniSofkiano, historialAsignacionSofkianoDTO.dniSofkiano());
        assertEquals(stringTest, historialAsignacionSofkianoDTO.nombreCompletoSofkiano());
        assertEquals(nit, historialAsignacionSofkianoDTO.nitCliente());
        assertEquals(stringTest, historialAsignacionSofkianoDTO.razonSocialCliente());
        assertEquals(TipoMovimiento.SALIDA, historialAsignacionSofkianoDTO.tipoMovimiento());
    }
}