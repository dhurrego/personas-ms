package co.com.sofka.usecase.sofkiano;

import co.com.sofka.model.estadisticas.gateways.CambioEstadoGateway;
import co.com.sofka.model.exception.negocio.BusinessException;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.dto.SofkianoARegistrarDTO;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import co.com.sofka.model.sofkiano.gateways.SofkianoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlmacenarEditarSofkianoUseCaseTest {

    private static final String TIPO_IDENTIFICACION = "CC";
    private static final String NUMERO_IDENTIFICACION = "1234567890";
    private static final String STRING_TEST = "TEST";

    @InjectMocks
    private AlmacenarEditarSofkianoUseCase useCase;

    @Mock
    private SofkianoRepository sofkianoRepository;

    @Mock
    private CambioEstadoGateway cambioEstadoGateway;

    private SofkianoARegistrarDTO sofkianoARegistrarDTO;
    private Sofkiano sofkiano;

    @BeforeEach
    void setUp() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                TIPO_IDENTIFICACION,
                NUMERO_IDENTIFICACION,
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST
        );

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
    void registrarSofkianoExitosamente() {
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.empty());
        when(sofkianoRepository.save(any(Sofkiano.class))).thenReturn(Mono.just(sofkiano));
        when(cambioEstadoGateway.reportarCambioEstadoSofkiano(any())).thenReturn(Mono.just(Boolean.TRUE));

        spy(sofkianoRepository);

        StepVerifier.create(useCase.registrarSofkiano(sofkianoARegistrarDTO))
                .assertNext(sofkianoDTOResult -> {
                    assertEquals(TIPO_IDENTIFICACION, sofkianoDTOResult.tipoIdentificacion());
                    assertEquals(NUMERO_IDENTIFICACION, sofkianoDTOResult.numeroIdentificacion());
                    assertTrue(sofkianoDTOResult.activo());
                    assertEquals(STRING_TEST, sofkianoDTOResult.direccion());
                })
                .verifyComplete();

        verify(sofkianoRepository, times(1)).findById(anyString());
        verify(sofkianoRepository, times(1)).save(any(Sofkiano.class));
        verify(cambioEstadoGateway, times(1)).reportarCambioEstadoSofkiano(any());
    }

    @Test
    void registroSofkianoFallidoYaExiste() {
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.just(sofkiano));

        spy(sofkianoRepository);

        StepVerifier.create(useCase.registrarSofkiano(sofkianoARegistrarDTO))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.registrarSofkiano(sofkianoARegistrarDTO))
                .expectErrorMessage(BusinessException.Tipo.ERROR_SOFKIANDO_YA_EXISTE.getMessage())
                .verify();

        verify(sofkianoRepository, times(2)).findById(anyString());
        verify(sofkianoRepository, times(0)).save(any(Sofkiano.class));
    }

    @Test
    void actualizarSofkianoExitosamente() {
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.just(sofkiano));
        when(sofkianoRepository.save(any(Sofkiano.class))).thenReturn(Mono.just(sofkiano));

        spy(sofkianoRepository);

        StepVerifier.create(useCase.actualizarSofkiano(sofkianoARegistrarDTO))
                .assertNext(sofkianoDTOResult -> {
                    assertEquals(TIPO_IDENTIFICACION, sofkianoDTOResult.tipoIdentificacion());
                    assertEquals(NUMERO_IDENTIFICACION, sofkianoDTOResult.numeroIdentificacion());
                    assertTrue(sofkianoDTOResult.activo());
                    assertEquals(STRING_TEST, sofkianoDTOResult.direccion());
                })
                .verifyComplete();

        verify(sofkianoRepository, times(1)).findById(anyString());
        verify(sofkianoRepository, times(1)).save(any(Sofkiano.class));
    }

    @Test
    void actualizacionSofkianoFallidoExisteInactivo() {

        sofkiano = sofkiano.toBuilder().activo(false).build();

        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.just(sofkiano));

        spy(sofkianoRepository);

        StepVerifier.create(useCase.actualizarSofkiano(sofkianoARegistrarDTO))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.actualizarSofkiano(sofkianoARegistrarDTO))
                .expectErrorMessage(BusinessException.Tipo.ERROR_SOFKIANO_INACTIVO.getMessage())
                .verify();

        verify(sofkianoRepository, times(2)).findById(anyString());
        verify(sofkianoRepository, times(0)).save(any(Sofkiano.class));
    }

    @Test
    void actualizacionSofkianoFallidoNoEncontrado() {

        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.empty());

        spy(sofkianoRepository);

        StepVerifier.create(useCase.actualizarSofkiano(sofkianoARegistrarDTO))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.actualizarSofkiano(sofkianoARegistrarDTO))
                .expectErrorMessage(BusinessException.Tipo.ERROR_SOFKIANO_NO_ENCONTRADO.getMessage())
                .verify();

        verify(sofkianoRepository, times(2)).findById(anyString());
        verify(sofkianoRepository, times(0)).save(any(Sofkiano.class));
    }
}