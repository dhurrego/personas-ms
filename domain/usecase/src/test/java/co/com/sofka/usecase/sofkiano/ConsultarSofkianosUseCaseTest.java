package co.com.sofka.usecase.sofkiano;

import co.com.sofka.model.exception.negocio.BusinessException;
import co.com.sofka.model.sofkiano.ConsolidadoAsignacionSofkiano;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import co.com.sofka.model.sofkiano.gateways.SofkianoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarSofkianosUseCaseTest {

    private static final String TIPO_IDENTIFICACION = "CC";
    private static final String NUMERO_IDENTIFICACION = "1234567890";
    private static final String STRING_TEST = "TEST";

    @InjectMocks
    private ConsultarSofkianosUseCase useCase;

    @Mock
    private SofkianoRepository sofkianoRepository;

    private Sofkiano sofkiano;

    @BeforeEach
    void setUp() {
        sofkiano = Sofkiano.builder()
                .dni(TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION))
                .tipoIdentificacion(TipoIdentificacion.valueOf(TIPO_IDENTIFICACION))
                .numeroIdentificacion(NUMERO_IDENTIFICACION)
                .primerNombre(STRING_TEST)
                .segundoNombre(Optional.of(STRING_TEST))
                .primerApellido(STRING_TEST)
                .segundoApellido(Optional.of(STRING_TEST))
                .activo(true)
                .direccion(STRING_TEST)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .fechaSalida(Optional.empty())
                .cliente(Optional.empty())
                .build();
    }

    @Test
    void listarTodos() {
        when(sofkianoRepository.findAll()).thenReturn(Flux.fromIterable(Collections.singletonList(sofkiano)));

        spy(sofkianoRepository);

        StepVerifier.create(useCase.listarTodos())
                .assertNext(sofkianoDTOResult -> {
                    assertEquals(TIPO_IDENTIFICACION, sofkianoDTOResult.tipoIdentificacion());
                    assertEquals(NUMERO_IDENTIFICACION, sofkianoDTOResult.numeroIdentificacion());
                    assertTrue(sofkianoDTOResult.activo());
                    assertEquals(STRING_TEST, sofkianoDTOResult.direccion());
                })
                .verifyComplete();

        verify(sofkianoRepository, times(1)).findAll();
    }

    @Test
    void listarPorDniSofkianoExistente() {
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.just(sofkiano));

        spy(sofkianoRepository);

        StepVerifier.create(useCase.listarPorDni(TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION)))
                .assertNext(sofkianoDTOResult -> {
                    assertEquals(TIPO_IDENTIFICACION, sofkianoDTOResult.tipoIdentificacion());
                    assertEquals(NUMERO_IDENTIFICACION, sofkianoDTOResult.numeroIdentificacion());
                    assertTrue(sofkianoDTOResult.activo());
                    assertEquals(STRING_TEST, sofkianoDTOResult.direccion());
                })
                .verifyComplete();

        verify(sofkianoRepository, times(1)).findById(anyString());
    }

    @Test
    void listarPorDniFallidoSofkianoNoExiste() {
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.empty());

        spy(sofkianoRepository);

        StepVerifier.create(useCase.listarPorDni(TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION)))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.listarPorDni(TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION)))
                .expectErrorMessage(BusinessException.Tipo.ERROR_SOFKIANO_NO_ENCONTRADO.getMessage())
                .verify();

        verify(sofkianoRepository, times(2)).findById(anyString());
    }

    @Test
    void obtenerConsolidadoAsignacion() {
        when(sofkianoRepository.obtenerConsolidadoAsignacion())
                .thenReturn(Mono.just( new ConsolidadoAsignacionSofkiano(0, 0, 0)));

        spy(sofkianoRepository);

        StepVerifier.create(useCase.obtenerConsolidadoAsignacion())
                .assertNext(consolidadoResult -> {
                    assertEquals(0, consolidadoResult.conAsignacion());
                    assertEquals(0, consolidadoResult.sinAsignacion());
                    assertEquals(0, consolidadoResult.totalSofkianos());
                })
                .verifyComplete();

        verify(sofkianoRepository, times(1)).obtenerConsolidadoAsignacion();
    }
}