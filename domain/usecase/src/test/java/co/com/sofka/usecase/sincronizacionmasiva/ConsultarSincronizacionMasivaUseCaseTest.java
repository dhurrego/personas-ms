package co.com.sofka.usecase.sincronizacionmasiva;

import co.com.sofka.model.exception.negocio.BusinessException;
import co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion;
import co.com.sofka.model.sincronizacionmasiva.SincronizacionMasiva;
import co.com.sofka.model.sincronizacionmasiva.gateways.SincronizacionMasivaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.UUID;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_PROCESO_SINCRONIZACION_MASIVA_NO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarSincronizacionMasivaUseCaseTest {

    private static final String ID_SINCRONIZACION = UUID.randomUUID().toString();

    @InjectMocks
    private ConsultarSincronizacionMasivaUseCase useCase;

    @Mock
    private SincronizacionMasivaRepository sincronizacionMasivaRepository;

    private SincronizacionMasiva sincronizacionMasiva;

    @BeforeEach
    void setUp() {
        sincronizacionMasiva = SincronizacionMasiva.builder()
                .idSincronizacion(ID_SINCRONIZACION)
                .estado(EstadoSincronizacion.CREADO)
                .ejecucionesExitosas(0)
                .ejecucionesFallidas(0)
                .detallesSincronizacion(Collections.emptyList())
                .build();
    }

    @Test
    void consultarTodasLasSincronizaciones() {
        when(sincronizacionMasivaRepository.findAll()).thenReturn(Flux.empty());

        spy(sincronizacionMasivaRepository);

        StepVerifier.create(useCase.consultarTodas())
                .expectNextCount(0)
                .verifyComplete();

        verify(sincronizacionMasivaRepository, times(1)).findAll();
    }

    @Test
    void consultarSincronizacionExitosa() {
        when(sincronizacionMasivaRepository.findById(anyString())).thenReturn(Mono.just(sincronizacionMasiva));

        spy(sincronizacionMasivaRepository);

        StepVerifier.create(useCase.consultarSincronizacion(ID_SINCRONIZACION))
                .assertNext(sincronizacionMasivaDTO -> {
                    assertEquals(ID_SINCRONIZACION, sincronizacionMasivaDTO.idSincronizacion());
                    assertEquals(EstadoSincronizacion.CREADO, sincronizacionMasivaDTO.estado());
                    assertEquals(0, sincronizacionMasivaDTO.ejecucionesExitosas());
                    assertEquals(0, sincronizacionMasivaDTO.ejecucionesFallidas());
                    assertEquals(0, sincronizacionMasivaDTO.detallesSincronizacion().size());
                })
                .verifyComplete();

        verify(sincronizacionMasivaRepository, times(1)).findById(anyString());
    }

    @Test
    void consultarSincronizacionFallidaProcesoNoEncontrado() {
        when(sincronizacionMasivaRepository.findById(anyString())).thenReturn(Mono.empty());

        spy(sincronizacionMasivaRepository);

        StepVerifier.create(useCase.consultarSincronizacion(ID_SINCRONIZACION))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.consultarSincronizacion(ID_SINCRONIZACION))
                .expectErrorMessage(ERROR_PROCESO_SINCRONIZACION_MASIVA_NO_ENCONTRADO.getMessage())
                .verify();

        verify(sincronizacionMasivaRepository, times(2)).findById(anyString());
    }
}