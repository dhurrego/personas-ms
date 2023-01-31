package co.com.sofka.sb.listener;

import co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion;
import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import co.com.sofka.usecase.sincronizacionmasiva.ProcesarSincronizacionMasivaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SincronizacionMasivaMessagesListenerTest {

    private static final String ID_SINCRONIZACION = UUID.randomUUID().toString();

    @InjectMocks
    private SincronizacionMasivaMessagesListener listener;

    @Mock
    private ProcesarSincronizacionMasivaUseCase procesarSincronizacionMasivaUseCase;

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
    void recibiendoMensajeSincronizacion() {
        when(procesarSincronizacionMasivaUseCase.sincronizar(any())).thenReturn(Mono.just(Boolean.TRUE));

        listener.process(sincronizacionMasivaDTO);

        verify(procesarSincronizacionMasivaUseCase, times(1)).sincronizar(any());
    }

    @Test
    void recibiendoMensajeSincronizacionPeroAlProcesoOcurreUnError() {
        when(procesarSincronizacionMasivaUseCase.sincronizar(any())).thenReturn(Mono.error(new RuntimeException("Error")));

        listener.process(sincronizacionMasivaDTO);

        verify(procesarSincronizacionMasivaUseCase, times(1)).sincronizar(any());
    }
}