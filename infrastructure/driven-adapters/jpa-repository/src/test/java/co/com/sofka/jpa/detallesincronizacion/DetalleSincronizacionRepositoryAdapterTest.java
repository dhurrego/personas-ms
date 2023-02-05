package co.com.sofka.jpa.detallesincronizacion;

import co.com.sofka.jpa.sincronizacionmasiva.SincronizacionMasivaData;
import co.com.sofka.model.detallesincronizacion.DetalleSincronizacion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DetalleSincronizacionRepositoryAdapterTest {

    private static final String ID_SINCRONIZACION = UUID.randomUUID().toString();
    private static final int ID_DETALLE = 1;
    private static final String DESCRIPCION_FALLIDO = "Error";

    @InjectMocks
    private DetalleSincronizacionRepositoryAdapter detalleSincronizacionRepositoryAdapter;

    @Test
    void toData() {
        DetalleSincronizacion detalleSincronizacion = DetalleSincronizacion.builder()
                .idDetalle(ID_DETALLE)
                .idSincronizacion(ID_SINCRONIZACION)
                .descripcionFallido(DESCRIPCION_FALLIDO)
                .build();

        DetalleSincronizacionData data = detalleSincronizacionRepositoryAdapter.toData(detalleSincronizacion);

        assertEquals(ID_SINCRONIZACION, data.getSincronizacionMasivaData().getIdSincronizacion());
        assertEquals(ID_DETALLE, data.getIdDetalle());
        assertEquals(DESCRIPCION_FALLIDO, data.getDescripcionFallido());
    }

    @Test
    void toEntity() {
        DetalleSincronizacionData detalleSincronizacionData = DetalleSincronizacionData.builder()
                .idDetalle(ID_DETALLE)
                .sincronizacionMasivaData(SincronizacionMasivaData.builder()
                        .idSincronizacion(ID_SINCRONIZACION)
                        .build())
                .descripcionFallido(DESCRIPCION_FALLIDO)
                .build();

        DetalleSincronizacion entity = detalleSincronizacionRepositoryAdapter.toEntity(detalleSincronizacionData);

        assertEquals(ID_SINCRONIZACION, entity.getIdSincronizacion());
        assertEquals(ID_DETALLE, entity.getIdDetalle());
        assertEquals(DESCRIPCION_FALLIDO, entity.getDescripcionFallido());
    }
}