package co.com.sofka.model.sincronizacionmasiva.factoria;

import co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion;
import co.com.sofka.model.sincronizacionmasiva.SincronizacionMasiva;
import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SincronizacionMasivaDTOFactoryTest {

    @Test
    void crearSincronizacionMasivaDTO() {
        final String idSincronizacion = UUID.randomUUID().toString();

        SincronizacionMasiva sincronizacionMasiva = SincronizacionMasivaFactory
                .crearSincronizacionMasiva(idSincronizacion);

        SincronizacionMasivaDTO sincronizacionMasivaDTO = SincronizacionMasivaDTOFactory
                .crearSincronizacionMasivaDTO(sincronizacionMasiva);

        assertEquals(idSincronizacion, sincronizacionMasivaDTO.idSincronizacion());
        assertEquals(EstadoSincronizacion.CREADO, sincronizacionMasivaDTO.estado());
        assertNotNull(sincronizacionMasivaDTO.ejecucionesExitosas());
        assertNotNull(sincronizacionMasivaDTO.ejecucionesFallidas());
        assertEquals(0, sincronizacionMasivaDTO.detallesSincronizacion().size());
    }
}