package co.com.sofka.broker.estadisticas.historialcambioestado;

import co.com.sofka.broker.estadisticas.historialcambioestado.dto.AgregarHistorialCambioEstadoDTO;
import co.com.sofka.model.estadisticas.dto.HistorialCambioEstadoDTO;
import co.com.sofka.model.estadisticas.enums.TipoMovimiento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class HistorialCambioEstadoSofkianoAsyncAdapterTest {

    @InjectMocks
    private HistorialCambioEstadoSofkianoAsyncAdapter historialCambioEstadoSofkianoAsyncAdapter;

    @Mock
    private JmsTemplate jmsTemplate;

    @Test
    void reportarCambioEstadoSofkianoExitosamente() {
        HistorialCambioEstadoDTO historialCambioEstadoDTO = HistorialCambioEstadoDTO.builder()
                .nombreCompletoSofkiano("TEST")
                .dniSofkiano("CC1231233")
                .tipoMovimiento(TipoMovimiento.INGRESO)
                .build();

        doNothing().when(jmsTemplate).convertAndSend(anyString(), any(AgregarHistorialCambioEstadoDTO.class));

        StepVerifier.create(historialCambioEstadoSofkianoAsyncAdapter
                        .reportarCambioEstadoSofkiano(historialCambioEstadoDTO))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void reportarCambioEstadoSofkianoFallido() {
        doThrow(new RuntimeException("Error")).when(jmsTemplate)
                .convertAndSend(anyString(), any(AgregarHistorialCambioEstadoDTO.class));

        HistorialCambioEstadoDTO historialCambioEstadoDTO = HistorialCambioEstadoDTO.builder()
                .nombreCompletoSofkiano("TEST")
                .dniSofkiano("CC1231233")
                .tipoMovimiento(TipoMovimiento.SALIDA)
                .build();

        StepVerifier.create(historialCambioEstadoSofkianoAsyncAdapter
                        .reportarCambioEstadoSofkiano(historialCambioEstadoDTO))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }
}