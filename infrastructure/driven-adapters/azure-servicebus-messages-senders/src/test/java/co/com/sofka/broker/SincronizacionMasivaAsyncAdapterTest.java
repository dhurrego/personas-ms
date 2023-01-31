package co.com.sofka.broker;

import co.com.sofka.model.exception.tecnico.TechnicalException;
import co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion;
import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.UUID;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_PUBLICANDO_MENSAJE_SINCRONIZACION_MASIVA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class SincronizacionMasivaAsyncAdapterTest {

    private static final String ID_SINCRONIZACION = UUID.randomUUID().toString();

    @InjectMocks
    private SincronizacionMasivaAsyncAdapter sincronizacionMasivaAsyncAdapter;

    @Mock
    private JmsTemplate jmsTemplate;

    private SincronizacionMasivaDTO sincronizacionMasivaDTO;

    @BeforeEach
    void setUp() {
        sincronizacionMasivaDTO = SincronizacionMasivaDTO.builder()
                .idSincronizacion(ID_SINCRONIZACION)
                .estado(EstadoSincronizacion.CREADO)
                .ejecucionesExitosas(0)
                .ejecucionesFallidas(0)
                .detallesSincronizacion(Collections.emptyList())
                .build();

    }

    @Test
    void solicitarInicioProcesoSincronizacion() {
        doNothing().when(jmsTemplate).convertAndSend(anyString(), any(SincronizacionMasivaDTO.class));

        StepVerifier.create(sincronizacionMasivaAsyncAdapter
                        .solicitarInicioProcesoSincronizacion(sincronizacionMasivaDTO))
                .assertNext(sincronizacionMasiva -> assertEquals(sincronizacionMasivaDTO, sincronizacionMasiva))
                .verifyComplete();
    }

    @Test
    void solicitarInicioProcesoSincronizacionFallidoConexionServicesBUS() {
        doThrow(new RuntimeException("Error")).when(jmsTemplate)
                .convertAndSend(anyString(), any(SincronizacionMasivaDTO.class));

        StepVerifier.create(sincronizacionMasivaAsyncAdapter
                        .solicitarInicioProcesoSincronizacion(sincronizacionMasivaDTO))
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(sincronizacionMasivaAsyncAdapter
                        .solicitarInicioProcesoSincronizacion(sincronizacionMasivaDTO))
                .expectErrorMessage(ERROR_PUBLICANDO_MENSAJE_SINCRONIZACION_MASIVA.getMessage())
                .verify();
    }
}