package co.com.sofka.model.detallesincronizacion.factoria;

import co.com.sofka.model.detallesincronizacion.DetalleSincronizacion;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DetalleSincronizacionFactoryTest {

    @Test
    void crearDetalle() {
        final String idSincronizacion = UUID.randomUUID().toString();
        final String descripcion = "Error";

        DetalleSincronizacion detalleSincronizacion = DetalleSincronizacionFactory.crearDetalle(idSincronizacion,
                descripcion);

        assertEquals(idSincronizacion, detalleSincronizacion.getIdSincronizacion());
        assertEquals(descripcion, detalleSincronizacion.getDescripcionFallido());
        assertNull(detalleSincronizacion.getIdDetalle());
    }
}