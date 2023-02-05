package co.com.sofka.usecase.sincronizacionmasiva;

import co.com.sofka.model.archivo.Archivo;
import co.com.sofka.model.archivo.dto.ArchivoDTO;
import co.com.sofka.model.archivo.gateways.AlmacenamientoArchivoRepository;
import co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion;
import co.com.sofka.model.sincronizacionmasiva.SincronizacionMasiva;
import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import co.com.sofka.model.sincronizacionmasiva.gateways.SincronizacionMasivaGateway;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitarSincronizacionMasivaUseCaseTest {

    private static final String ID_SINCRONIZACION = UUID.randomUUID().toString();

    @InjectMocks
    private SolicitarSincronizacionMasivaUseCase useCase;

    @Mock
    private AlmacenamientoArchivoRepository almacenamientoArchivoRepository;

    @Mock
    private SincronizacionMasivaRepository sincronizacionMasivaRepository;

    @Mock
    private SincronizacionMasivaGateway sincronizacionMasivaGateway;

    private ArchivoDTO archivoDTO;
    private SincronizacionMasiva sincronizacionMasiva;
    private SincronizacionMasivaDTO sincronizacionMasivaDTO;

    @BeforeEach
    void setUp() {
        archivoDTO = new ArchivoDTO("xlsx", Flux.empty());
        sincronizacionMasiva = SincronizacionMasiva.builder()
                .idSincronizacion(ID_SINCRONIZACION)
                .estado(EstadoSincronizacion.CREADO)
                .ejecucionesExitosas(0)
                .ejecucionesFallidas(0)
                .detallesSincronizacion(Collections.emptyList())
                .build();

        sincronizacionMasivaDTO = SincronizacionMasivaDTO.builder()
                .idSincronizacion(ID_SINCRONIZACION)
                .estado(EstadoSincronizacion.CREADO)
                .ejecucionesExitosas(0)
                .ejecucionesFallidas(0)
                .detallesSincronizacion(Collections.emptyList())
                .build();
    }

    @Test
    void solicitarSincronizacionExitosamente() {
        when(almacenamientoArchivoRepository.subir(any(Archivo.class)))
                .thenReturn(Mono.just(Boolean.TRUE));
        when(sincronizacionMasivaRepository.save(any(SincronizacionMasiva.class)))
                .thenReturn(Mono.just(sincronizacionMasiva));
        when(sincronizacionMasivaGateway.solicitarInicioProcesoSincronizacion(any(SincronizacionMasivaDTO.class)))
                .thenReturn(Mono.just(sincronizacionMasivaDTO));

        StepVerifier.create(useCase.solicitarSincronizacion(archivoDTO))
                .assertNext(sincronizacionMasivaDTO -> {
                    assertEquals(ID_SINCRONIZACION, sincronizacionMasivaDTO.idSincronizacion());
                    assertEquals(EstadoSincronizacion.CREADO, sincronizacionMasivaDTO.estado());
                    assertEquals(0, sincronizacionMasivaDTO.ejecucionesExitosas());
                    assertEquals(0, sincronizacionMasivaDTO.ejecucionesFallidas());
                    assertEquals(0, sincronizacionMasivaDTO.detallesSincronizacion().size());
                })
                .verifyComplete();

        verify(almacenamientoArchivoRepository, times(1)).subir(any());
        verify(sincronizacionMasivaRepository, times(1)).save(any());
        verify(sincronizacionMasivaGateway, times(1)).solicitarInicioProcesoSincronizacion(any());
    }
}