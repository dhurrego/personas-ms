package co.com.sofka.broker.estadisticas.historialasignacionsofkiano;

import co.com.sofka.broker.estadisticas.historialasignacionsofkiano.dto.AgregarHistorialAsignacionSofkianoDTO;
import co.com.sofka.model.estadisticas.dto.HistorialAsignacionSofkianoDTO;
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
class HistorialCambioAsignacionSofkianoAsyncAdapterTest {

    @InjectMocks
    private HistorialCambioAsignacionSofkianoAsyncAdapter historialCambioAsignacionSofkianoAsyncAdapter;

    @Mock
    private JmsTemplate jmsTemplate;

    @Test
    void reportarCambioAsignacionSofkianoExitosamente() {
        HistorialAsignacionSofkianoDTO historialAsignacionSofkianoDTO = HistorialAsignacionSofkianoDTO.builder()
                .nombreCompletoSofkiano("TEST")
                .dniSofkiano("CC1231233")
                .nitCliente("890512356")
                .razonSocialCliente("TEST")
                .tipoMovimiento(TipoMovimiento.INGRESO)
                .build();

        doNothing().when(jmsTemplate).convertAndSend(anyString(), any(AgregarHistorialAsignacionSofkianoDTO.class));

        StepVerifier.create(historialCambioAsignacionSofkianoAsyncAdapter
                        .reportarCambioAsignacionSofkiano(historialAsignacionSofkianoDTO))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void reportarCambioAsignacionSofkianoFaliido() {
        HistorialAsignacionSofkianoDTO historialAsignacionSofkianoDTO = HistorialAsignacionSofkianoDTO.builder()
                .nombreCompletoSofkiano("TEST")
                .dniSofkiano("CC1231233")
                .nitCliente("890512356")
                .razonSocialCliente("TEST")
                .tipoMovimiento(TipoMovimiento.SALIDA)
                .build();

        doThrow(new RuntimeException("Error")).when(jmsTemplate)
                .convertAndSend(anyString(), any(AgregarHistorialAsignacionSofkianoDTO.class));

        StepVerifier.create(historialCambioAsignacionSofkianoAsyncAdapter
                        .reportarCambioAsignacionSofkiano(historialAsignacionSofkianoDTO))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }
}