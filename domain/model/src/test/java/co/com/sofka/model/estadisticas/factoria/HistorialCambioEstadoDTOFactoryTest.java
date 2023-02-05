package co.com.sofka.model.estadisticas.factoria;

import co.com.sofka.model.estadisticas.dto.HistorialCambioEstadoDTO;
import co.com.sofka.model.estadisticas.enums.TipoMovimiento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistorialCambioEstadoDTOFactoryTest {

    @Test
    void crearAgregarHistorialCambioEstadoDTO() {
        final String dniSofkiano = "CC123133";
        final String nombreSofkiano = "TEST";

        HistorialCambioEstadoDTO historialCambioEstadoDTO = HistorialCambioEstadoDTOFactory
                .crearAgregarHistorialCambioEstadoDTO(dniSofkiano, nombreSofkiano, TipoMovimiento.INGRESO);

        assertEquals(dniSofkiano, historialCambioEstadoDTO.dniSofkiano());
        assertEquals(nombreSofkiano, historialCambioEstadoDTO.nombreCompletoSofkiano());
        assertEquals(TipoMovimiento.INGRESO, historialCambioEstadoDTO.tipoMovimiento());
    }
}