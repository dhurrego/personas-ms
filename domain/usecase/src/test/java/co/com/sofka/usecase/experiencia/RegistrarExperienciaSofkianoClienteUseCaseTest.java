package co.com.sofka.usecase.experiencia;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.cliente.dto.ClienteDTO;
import co.com.sofka.model.exception.negocio.BusinessException;
import co.com.sofka.model.experiencia.ExperienciaSofkianoCliente;
import co.com.sofka.model.experiencia.dto.RegistrarExperienciaSofkianoDTO;
import co.com.sofka.model.experiencia.gateways.ExperienciaSofkianoClienteRepository;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.dto.SofkianoDTO;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import co.com.sofka.usecase.sofkiano.ConsultarSofkianosUseCase;
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

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_SOFKIANO_NO_ASIGNADO_AL_CLIENTE;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarExperienciaSofkianoClienteUseCaseTest {

    private static final Integer ID_EXPERIENCIA = 1;
    private static final String STRING_TEST = "TESTING";
    private static final String TIPO_IDENTIFICACION = "CC";
    private static final String NUMERO_IDENTIFICACION = "123123123";
    private static final int NIVEL_SATISFACCION = 10;
    private static final String NIT = "8905625451";

    @InjectMocks
    private RegistrarExperienciaSofkianoClienteUseCase registrarUseCase;

    @Mock
    private ConsultarSofkianosUseCase consultarSofkianosUseCase;

    @Mock
    private ExperienciaSofkianoClienteRepository repository;

    private RegistrarExperienciaSofkianoDTO registrarExperienciaSofkianoDTO;
    private ExperienciaSofkianoCliente experienciaSofkianoCliente;
    private SofkianoDTO sofkianoDTO;

    @BeforeEach
    void setUp() {
        registrarExperienciaSofkianoDTO = new RegistrarExperienciaSofkianoDTO(
                TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION),
                NIT,
                NIVEL_SATISFACCION,
                STRING_TEST
        );

        Cliente cliente = Cliente.builder()
                .nit(NIT)
                .razonSocial(STRING_TEST)
                .build();

        ClienteDTO clienteDTO = ClienteDTO.builder()
                .nit(NIT)
                .razonSocial(STRING_TEST)
                .build();

        Sofkiano sofkiano = Sofkiano.builder()
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
                .cliente(Optional.of(cliente))
                .build();

        experienciaSofkianoCliente = ExperienciaSofkianoCliente.builder()
                .idExperiencia(ID_EXPERIENCIA)
                .descripcion(STRING_TEST)
                .fechaCreacion(LocalDateTime.now())
                .sofkiano(sofkiano)
                .cliente(cliente)
                .nivelSatisfaccion(NIVEL_SATISFACCION)
                .build();

        sofkianoDTO = SofkianoDTO.builder()
                .tipoIdentificacion(TIPO_IDENTIFICACION)
                .numeroIdentificacion(NUMERO_IDENTIFICACION)
                .primerNombre(STRING_TEST)
                .segundoNombre(Optional.of(STRING_TEST))
                .primerApellido(STRING_TEST)
                .segundoApellido(Optional.of(STRING_TEST))
                .activo(true)
                .direccion(STRING_TEST)
                .cliente(Optional.of(clienteDTO))
                .build();
    }

    @Test
    void registrarExperienciaClienteExtisomente() {
        when(repository.save(any(ExperienciaSofkianoCliente.class))).thenReturn(Mono.just(experienciaSofkianoCliente));
        when(consultarSofkianosUseCase.listarPorDni(anyString())).thenReturn(Mono.just(sofkianoDTO));

        spy(repository);
        spy(consultarSofkianosUseCase);

        StepVerifier.create(registrarUseCase.registrarExperienciaCliente(registrarExperienciaSofkianoDTO))
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

        verify(repository, times(1)).save(any(ExperienciaSofkianoCliente.class));
        verify(consultarSofkianosUseCase, times(1)).listarPorDni(anyString());
    }

    @Test
    void registrarExperienciaClienteFallidoClienteNoAsignadoAlSofkiano() {

        sofkianoDTO = SofkianoDTO.builder()
                .tipoIdentificacion(TIPO_IDENTIFICACION)
                .numeroIdentificacion(NUMERO_IDENTIFICACION)
                .primerNombre(STRING_TEST)
                .segundoNombre(Optional.of(STRING_TEST))
                .primerApellido(STRING_TEST)
                .segundoApellido(Optional.of(STRING_TEST))
                .activo(true)
                .direccion(STRING_TEST)
                .cliente(Optional.empty())
                .build();

        when(consultarSofkianosUseCase.listarPorDni(anyString())).thenReturn(Mono.just(sofkianoDTO));

        spy(repository);
        spy(consultarSofkianosUseCase);

        StepVerifier.create(registrarUseCase.registrarExperienciaCliente(registrarExperienciaSofkianoDTO))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(registrarUseCase.registrarExperienciaCliente(registrarExperienciaSofkianoDTO))
                .expectErrorMessage(ERROR_SOFKIANO_NO_ASIGNADO_AL_CLIENTE.getMessage())
                .verify();

        verify(repository, times(0)).save(any(ExperienciaSofkianoCliente.class));
        verify(consultarSofkianosUseCase, times(2)).listarPorDni(anyString());
    }
}