package co.com.sofka.jpa.sincronizacionmasiva;

import co.com.sofka.model.exception.tecnico.TechnicalException;
import co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion;
import co.com.sofka.model.sincronizacionmasiva.SincronizacionMasiva;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.UUID;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_COMUNICACION_BASE_DATOS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SincronizacionMasivaRepositoryAdapterTest {

    private static final String ID_SINCRONIZACION = UUID.randomUUID().toString();

    @InjectMocks
    private SincronizacionMasivaRepositoryAdapter sincronizacionMasivaRepositoryAdapter;

    @Mock
    private SincronizacionMasivaDataRepository repository;

    private SincronizacionMasiva sincronizacionMasiva;
    private SincronizacionMasivaData sincronizacionMasivaData;

    @BeforeEach
    void setUp() {
        sincronizacionMasiva = SincronizacionMasiva.builder()
                .idSincronizacion(ID_SINCRONIZACION)
                .estado(EstadoSincronizacion.CREADO)
                .ejecucionesExitosas(0)
                .ejecucionesFallidas(0)
                .detallesSincronizacion(Collections.emptyList())
                .build();

        sincronizacionMasivaData = SincronizacionMasivaData.builder()
                .idSincronizacion(ID_SINCRONIZACION)
                .estado(EstadoSincronizacion.CREADO)
                .ejecucionesExitosas(0)
                .ejecucionesFallidas(0)
                .detallesSincronizacionData(Collections.emptyList())
                .build();
    }

    @Test
    void toData() {
        SincronizacionMasivaData sincronizacionMasivaData = sincronizacionMasivaRepositoryAdapter
                .toData(sincronizacionMasiva);

        assertEquals(ID_SINCRONIZACION, sincronizacionMasivaData.getIdSincronizacion());
        assertEquals(EstadoSincronizacion.CREADO, sincronizacionMasivaData.getEstado());
        assertEquals(0, sincronizacionMasivaData.getEjecucionesExitosas());
        assertEquals(0, sincronizacionMasivaData.getEjecucionesFallidas());
        assertEquals(0, sincronizacionMasivaData.getDetallesSincronizacionData().size());
    }

    @Test
    void toDataDetalleSincronizacionEsNula() {
        sincronizacionMasiva = sincronizacionMasiva.toBuilder()
                .detallesSincronizacion(null)
                .build();

        SincronizacionMasivaData sincronizacionMasivaData = sincronizacionMasivaRepositoryAdapter
                .toData(sincronizacionMasiva);

        assertEquals(ID_SINCRONIZACION, sincronizacionMasivaData.getIdSincronizacion());
        assertEquals(EstadoSincronizacion.CREADO, sincronizacionMasivaData.getEstado());
        assertEquals(0, sincronizacionMasivaData.getEjecucionesExitosas());
        assertEquals(0, sincronizacionMasivaData.getEjecucionesFallidas());
        assertEquals(0, sincronizacionMasivaData.getDetallesSincronizacionData().size());
    }

    @Test
    void toEntity() {
        SincronizacionMasiva sincronizacionMasiva = sincronizacionMasivaRepositoryAdapter
                .toEntity(sincronizacionMasivaData);

        assertEquals(ID_SINCRONIZACION, sincronizacionMasiva.getIdSincronizacion());
        assertEquals(EstadoSincronizacion.CREADO, sincronizacionMasiva.getEstado());
        assertEquals(0, sincronizacionMasiva.getEjecucionesExitosas());
        assertEquals(0, sincronizacionMasiva.getEjecucionesFallidas());
        assertEquals(0, sincronizacionMasiva.getDetallesSincronizacion().size());
    }

    @Test
    void toEntityDetalleSincronizacionEsNula() {
        sincronizacionMasivaData = sincronizacionMasivaData.toBuilder()
                .detallesSincronizacionData(null)
                .build();

        SincronizacionMasiva sincronizacionMasiva = sincronizacionMasivaRepositoryAdapter
                .toEntity(sincronizacionMasivaData);

        assertEquals(ID_SINCRONIZACION, sincronizacionMasiva.getIdSincronizacion());
        assertEquals(EstadoSincronizacion.CREADO, sincronizacionMasiva.getEstado());
        assertEquals(0, sincronizacionMasiva.getEjecucionesExitosas());
        assertEquals(0, sincronizacionMasiva.getEjecucionesFallidas());
        assertEquals(0, sincronizacionMasiva.getDetallesSincronizacion().size());
    }

    @Test
    void incrementarEjecucionesFallidasExitosamente() {
        when(repository.incrementarEjecucionesFallidas(anyString())).thenReturn(1);

        StepVerifier.create(sincronizacionMasivaRepositoryAdapter.incrementarEjecucionesFallidas(ID_SINCRONIZACION))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void finalizarSincronizacionMasivaExitosamente() {
        when(repository.finalizarSincronizacionMasiva(anyString(), anyInt())).thenReturn(1);

        StepVerifier.create(sincronizacionMasivaRepositoryAdapter.finalizarSincronizacionMasiva(ID_SINCRONIZACION, 1))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void rechazarSincronizacionMasivaExitosamente() {
        when(repository.rechazarSincronizacionMasiva(anyString())).thenReturn(1);

        StepVerifier.create(sincronizacionMasivaRepositoryAdapter.rechazarSincronizacionMasiva(ID_SINCRONIZACION))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void incrementarEjecucionesFallidasErrorBaseDeDatos() {
        when(repository.incrementarEjecucionesFallidas(anyString())).thenThrow(new RuntimeException("Error"));

        StepVerifier.create(sincronizacionMasivaRepositoryAdapter.incrementarEjecucionesFallidas(ID_SINCRONIZACION))
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(sincronizacionMasivaRepositoryAdapter.incrementarEjecucionesFallidas(ID_SINCRONIZACION))
                .expectErrorMessage(ERROR_COMUNICACION_BASE_DATOS.getMessage())
                .verify();
    }

    @Test
    void finalizarSincronizacionMasivaErrorBaseDatos() {
        when(repository.finalizarSincronizacionMasiva(anyString(), anyInt())).thenThrow(new RuntimeException("Error"));

        StepVerifier.create(sincronizacionMasivaRepositoryAdapter.finalizarSincronizacionMasiva(ID_SINCRONIZACION, 1))
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(sincronizacionMasivaRepositoryAdapter.finalizarSincronizacionMasiva(ID_SINCRONIZACION, 1))
                .expectErrorMessage(ERROR_COMUNICACION_BASE_DATOS.getMessage())
                .verify();
    }

    @Test
    void rechazarSincronizacionMasivaErrorBaseDatos() {
        when(repository.rechazarSincronizacionMasiva(anyString())).thenThrow(new RuntimeException("Error"));

        StepVerifier.create(sincronizacionMasivaRepositoryAdapter.rechazarSincronizacionMasiva(ID_SINCRONIZACION))
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(sincronizacionMasivaRepositoryAdapter.rechazarSincronizacionMasiva(ID_SINCRONIZACION))
                .expectErrorMessage(ERROR_COMUNICACION_BASE_DATOS.getMessage())
                .verify();
    }
}