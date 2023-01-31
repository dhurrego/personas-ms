package co.com.sofka.model.sincronizacionmasiva.factoria;

import co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion;
import co.com.sofka.model.sincronizacionmasiva.SincronizacionMasiva;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SincronizacionMasivaFactoryTest {

    @Test
    void crearSincronizacionMasiva() {
        final String idSincronizacion = UUID.randomUUID().toString();
        SincronizacionMasiva sincronizacionMasiva = SincronizacionMasivaFactory.crearSincronizacionMasiva(idSincronizacion);

        assertEquals(idSincronizacion, sincronizacionMasiva.getIdSincronizacion());
        assertEquals(EstadoSincronizacion.CREADO, sincronizacionMasiva.getEstado());
        assertNotNull(sincronizacionMasiva.getEjecucionesExitosas());
        assertNotNull(sincronizacionMasiva.getEjecucionesFallidas());
    }
}