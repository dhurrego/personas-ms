package co.com.sofka.usecase.sofkiano;

import co.com.sofka.model.exception.negocio.BusinessException;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import co.com.sofka.model.sofkiano.gateways.SofkianoRepository;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CambiarEstadoSofkianoUseCaseTest {

    private static final String DNI = "CC1234567890";
    private static final String TIPO_IDENTIFICACION = "CC";
    private static final String NUMERO_IDENTIFICACION = "1234567890";
    private static final String STRING_TEST = "TEST";

    @InjectMocks
    private CambiarEstadoSofkianoUseCase useCase;

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
    void inactivarSofkianoExitosamente() {
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.just(sofkiano));
        when(sofkianoRepository.save(any(Sofkiano.class))).thenReturn(Mono.just(sofkiano));

        spy(sofkianoRepository);

        StepVerifier.create(useCase.inactivarSofkiano(DNI))
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

        verify(sofkianoRepository, times(1)).findById(anyString());
        verify(sofkianoRepository, times(1)).save(any(Sofkiano.class));
    }

    @Test
    void inactivacionSofkianoFallidaYaSeEncuentraInactivo() {
        sofkiano = sofkiano.toBuilder()
                .activo(false)
                .fechaSalida(Optional.of(LocalDateTime.now()))
                .build();

        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.just(sofkiano));

        spy(sofkianoRepository);

        StepVerifier.create(useCase.inactivarSofkiano(DNI))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.inactivarSofkiano(DNI))
                .expectErrorMessage(BusinessException.Tipo.ERROR_SOFKIANO_INACTIVO.getMessage())
                .verify();

        verify(sofkianoRepository, times(2)).findById(anyString());
        verify(sofkianoRepository, times(0)).save(any(Sofkiano.class));
    }

    @Test
    void inactivacionSofkianoFallidaNoEncontrado() {
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.empty());

        spy(sofkianoRepository);

        StepVerifier.create(useCase.inactivarSofkiano(DNI))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.inactivarSofkiano(DNI))
                .expectErrorMessage(BusinessException.Tipo.ERROR_SOFKIANO_NO_ENCONTRADO.getMessage())
                .verify();

        verify(sofkianoRepository, times(2)).findById(anyString());
        verify(sofkianoRepository, times(0)).save(any(Sofkiano.class));
    }

    @Test
    void activarSofkianoExitosamente() {
        sofkiano = sofkiano.toBuilder()
                .activo(false)
                .fechaSalida(Optional.of(LocalDateTime.now()))
                .build();

        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.just(sofkiano));
        when(sofkianoRepository.save(any(Sofkiano.class))).thenReturn(Mono.just(sofkiano));

        spy(sofkianoRepository);

        StepVerifier.create(useCase.activarSofkiano(DNI))
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

        verify(sofkianoRepository, times(1)).findById(anyString());
        verify(sofkianoRepository, times(1)).save(any(Sofkiano.class));
    }

    @Test
    void activarSofkianoFallidoYaActivo() {
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.just(sofkiano));

        spy(sofkianoRepository);

        StepVerifier.create(useCase.activarSofkiano(DNI))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.activarSofkiano(DNI))
                .expectErrorMessage(BusinessException.Tipo.ERROR_SOFKIANO_ACTIVO.getMessage())
                .verify();

        verify(sofkianoRepository, times(2)).findById(anyString());
        verify(sofkianoRepository, times(0)).save(any(Sofkiano.class));
    }

    @Test
    void activarSofkianoFallidoNoEncontrado() {
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.empty());

        spy(sofkianoRepository);

        StepVerifier.create(useCase.activarSofkiano(DNI))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.activarSofkiano(DNI))
                .expectErrorMessage(BusinessException.Tipo.ERROR_SOFKIANO_NO_ENCONTRADO.getMessage())
                .verify();

        verify(sofkianoRepository, times(2)).findById(anyString());
        verify(sofkianoRepository, times(0)).save(any(Sofkiano.class));
    }
}