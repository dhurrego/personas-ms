package co.com.sofka.usecase.sofkiano;

import co.com.sofka.model.cliente.dto.ClienteDTO;
import co.com.sofka.model.exception.negocio.BusinessException;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.dto.AsignarClienteDTO;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import co.com.sofka.model.sofkiano.gateways.SofkianoRepository;
import co.com.sofka.usecase.cliente.ConsultarClientesUseCase;
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
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ModificarAsignacionUseCaseTest {

    private static final String NIT = "9805655551";
    private static final String DNI = "CC1234567890";
    private static final String TIPO_IDENTIFICACION = "CC";
    private static final String NUMERO_IDENTIFICACION = "1234567890";
    private static final String STRING_TEST = "TEST";

    @InjectMocks
    private ModificarAsignacionUseCase modificarAsignacionUseCase;

    @Mock
    private SofkianoRepository sofkianoRepository;

    @Mock
    private ConsultarClientesUseCase consultarClientesUseCase;

    private Sofkiano sofkiano;

    private AsignarClienteDTO asignarClienteDTO;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        clienteDTO = ClienteDTO.builder()
                .nit(NIT)
                .razonSocial(STRING_TEST)
                .build();

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

        asignarClienteDTO = new AsignarClienteDTO(DNI, NIT);
    }

    @Test
    void asignarClienteExitosamente() {
        when(consultarClientesUseCase.listarPorNit(anyString())).thenReturn(Mono.just(clienteDTO));
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.just(sofkiano));
        when(sofkianoRepository.save(any(Sofkiano.class))).thenReturn(Mono.just(sofkiano));

        spy(sofkianoRepository);
        spy(consultarClientesUseCase);

        StepVerifier.create(modificarAsignacionUseCase.asignarCliente(asignarClienteDTO))
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

        verify(consultarClientesUseCase, times(1)).listarPorNit(anyString());
        verify(sofkianoRepository, times(1)).findById(anyString());
        verify(sofkianoRepository, times(1)).save(any(Sofkiano.class));
    }

    @Test
    void asignarClienteFallidoSofkianoInactivo() {

        sofkiano = sofkiano.toBuilder().activo(false).build();

        when(consultarClientesUseCase.listarPorNit(anyString())).thenReturn(Mono.just(clienteDTO));
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.just(sofkiano));

        spy(sofkianoRepository);
        spy(consultarClientesUseCase);

        StepVerifier.create(modificarAsignacionUseCase.asignarCliente(asignarClienteDTO))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(modificarAsignacionUseCase.asignarCliente(asignarClienteDTO))
                .expectErrorMessage(BusinessException.Tipo.ERROR_SOFKIANO_INACTIVO.getMessage())
                .verify();

        verify(consultarClientesUseCase, times(2)).listarPorNit(anyString());
        verify(sofkianoRepository, times(2)).findById(anyString());
        verify(sofkianoRepository, times(0)).save(any(Sofkiano.class));
    }

    @Test
    void asignarClienteFallidoSofkianoNoEncontrado() {
        when(consultarClientesUseCase.listarPorNit(anyString())).thenReturn(Mono.just(clienteDTO));
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.empty());

        spy(sofkianoRepository);
        spy(consultarClientesUseCase);

        StepVerifier.create(modificarAsignacionUseCase.asignarCliente(asignarClienteDTO))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(modificarAsignacionUseCase.asignarCliente(asignarClienteDTO))
                .expectErrorMessage(BusinessException.Tipo.ERROR_SOFKIANO_NO_ENCONTRADO.getMessage())
                .verify();

        verify(consultarClientesUseCase, times(2)).listarPorNit(anyString());
        verify(sofkianoRepository, times(2)).findById(anyString());
        verify(sofkianoRepository, times(0)).save(any(Sofkiano.class));
    }

    @Test
    void retirarAsignacionExitosamente() {
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.just(sofkiano));
        when(sofkianoRepository.save(any(Sofkiano.class))).thenReturn(Mono.just(sofkiano));

        spy(sofkianoRepository);

        StepVerifier.create(modificarAsignacionUseCase.retirarAsignacion(DNI))
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

        verify(sofkianoRepository, times(1)).findById(anyString());
        verify(sofkianoRepository, times(1)).save(any(Sofkiano.class));
    }

    @Test
    void retirarAsignacionFallidaSofkianoInactivo() {
        sofkiano = sofkiano.toBuilder().activo(false).build();

        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.just(sofkiano));

        spy(sofkianoRepository);

        StepVerifier.create(modificarAsignacionUseCase.retirarAsignacion(DNI))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(modificarAsignacionUseCase.retirarAsignacion(DNI))
                .expectErrorMessage(BusinessException.Tipo.ERROR_SOFKIANO_INACTIVO.getMessage())
                .verify();

        verify(sofkianoRepository, times(2)).findById(anyString());
        verify(sofkianoRepository, times(0)).save(any(Sofkiano.class));
    }

    @Test
    void retirarAsignacionFallidaSofkianoNoEncontrado() {
        when(sofkianoRepository.findById(anyString())).thenReturn(Mono.empty());

        spy(sofkianoRepository);

        StepVerifier.create(modificarAsignacionUseCase.retirarAsignacion(DNI))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(modificarAsignacionUseCase.retirarAsignacion(DNI))
                .expectErrorMessage(BusinessException.Tipo.ERROR_SOFKIANO_NO_ENCONTRADO.getMessage())
                .verify();

        verify(sofkianoRepository, times(2)).findById(anyString());
        verify(sofkianoRepository, times(0)).save(any(Sofkiano.class));
    }
}