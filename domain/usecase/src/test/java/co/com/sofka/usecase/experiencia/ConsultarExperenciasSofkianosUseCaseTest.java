package co.com.sofka.usecase.experiencia;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.exception.negocio.BusinessException;
import co.com.sofka.model.experiencia.ExperienciaSofkianoCliente;
import co.com.sofka.model.experiencia.PromedioCalificacionCliente;
import co.com.sofka.model.experiencia.gateways.ExperienciaSofkianoClienteRepository;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_EXPERIENCIAS_NO_ENCONTRADAS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarExperenciasSofkianosUseCaseTest {

    private static final Integer ID_EXPERIENCIA = 1;
    private static final String STRING_TEST = "TEST";
    private static final String TIPO_IDENTIFICACION = "CC";
    private static final String NUMERO_IDENTIFICACION = "123123123";
    private static final int NIVEL_SATISFACCION = 10;
    private static final String NIT = "8905625451";
    private String dni;

    @InjectMocks
    private ConsultarExperenciasSofkianosUseCase useCase;

    @Mock
    private ExperienciaSofkianoClienteRepository repository;

    private ExperienciaSofkianoCliente experienciaSofkianoCliente;

    @BeforeEach
    void setUp() {
        dni = TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION);

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
                .cliente(Optional.empty())
                .build();

        Cliente cliente = Cliente.builder()
                .nit(NIT)
                .razonSocial(STRING_TEST)
                .build();

        experienciaSofkianoCliente = ExperienciaSofkianoCliente.builder()
                .idExperiencia(ID_EXPERIENCIA)
                .descripcion(STRING_TEST)
                .fechaCreacion(LocalDateTime.now())
                .sofkiano(sofkiano)
                .cliente(cliente)
                .nivelSatisfaccion(NIVEL_SATISFACCION)
                .build();
    }

    @Test
    void consultarTodasLasExperienciasExitosamente() {
        when(repository.findAll()).thenReturn(Flux.fromIterable(Collections.singletonList(experienciaSofkianoCliente)));

        spy(repository);

        StepVerifier.create(useCase.consultarTodasLasExperiencias())
                .assertNext(experienciaSofkianoClienteDTO -> {
                    assertEquals(STRING_TEST, experienciaSofkianoClienteDTO.descripcion());
                    assertEquals(NIVEL_SATISFACCION, experienciaSofkianoClienteDTO.nivelSatisfaccion());
                    assertEquals(dni, experienciaSofkianoClienteDTO.dniSofkiano());
                    assertEquals(NIT, experienciaSofkianoClienteDTO.nitCliente());
                })
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    void consultarExperienciasPorSofkianoExitosamente() {
        when(repository.consultarExperienciasPorSofkiano(anyString()))
                .thenReturn(Flux.fromIterable(Collections.singletonList(experienciaSofkianoCliente)));

        spy(repository);

        StepVerifier.create(useCase.consultarExperienciasPorSofkiano(dni))
                .assertNext(experienciaSofkianoClienteDTO -> {
                    assertEquals(STRING_TEST, experienciaSofkianoClienteDTO.descripcion());
                    assertEquals(NIVEL_SATISFACCION, experienciaSofkianoClienteDTO.nivelSatisfaccion());
                    assertEquals(dni, experienciaSofkianoClienteDTO.dniSofkiano());
                    assertEquals(NIT, experienciaSofkianoClienteDTO.nitCliente());
                })
                .verifyComplete();

        verify(repository, times(1)).consultarExperienciasPorSofkiano(anyString());
    }

    @Test
    void consultarExperienciasPorSofkianoFallidaNoEncontrado() {
        when(repository.consultarExperienciasPorSofkiano(anyString()))
                .thenReturn(Flux.empty());

        spy(repository);

        StepVerifier.create(useCase.consultarExperienciasPorSofkiano(dni))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.consultarExperienciasPorSofkiano(dni))
                .expectErrorMessage(ERROR_EXPERIENCIAS_NO_ENCONTRADAS.getMessage())
                .verify();

        verify(repository, times(2)).consultarExperienciasPorSofkiano(anyString());
    }

    @Test
    void consultarExperienciasPorSofkianoYCliente() {
        when(repository.consultarExperienciasPorSofkianoYCliente(anyString(), anyString()))
                .thenReturn(Flux.fromIterable(Collections.singletonList(experienciaSofkianoCliente)));

        spy(repository);

        StepVerifier.create(useCase.consultarExperienciasPorSofkianoYCliente(dni, NIT))
                .assertNext(experienciaSofkianoClienteDTO -> {
                    assertEquals(STRING_TEST, experienciaSofkianoClienteDTO.descripcion());
                    assertEquals(NIVEL_SATISFACCION, experienciaSofkianoClienteDTO.nivelSatisfaccion());
                    assertEquals(dni, experienciaSofkianoClienteDTO.dniSofkiano());
                    assertEquals(NIT, experienciaSofkianoClienteDTO.nitCliente());
                })
                .verifyComplete();

        verify(repository, times(1))
                .consultarExperienciasPorSofkianoYCliente(anyString(), anyString());
    }

    @Test
    void consultarExperienciasPorSofkianoYClienteFallidaNoEncontrado() {
        when(repository.consultarExperienciasPorSofkianoYCliente(anyString(), anyString()))
                .thenReturn(Flux.empty());

        spy(repository);

        StepVerifier.create(useCase.consultarExperienciasPorSofkianoYCliente(dni, NIT))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.consultarExperienciasPorSofkianoYCliente(dni, NIT))
                .expectErrorMessage(ERROR_EXPERIENCIAS_NO_ENCONTRADAS.getMessage())
                .verify();

        verify(repository, times(2))
                .consultarExperienciasPorSofkianoYCliente(anyString(), anyString());
    }

    @Test
    void consultarExperienciasPorCliente() {
        when(repository.consultarExperienciasPorCliente(anyString()))
                .thenReturn(Flux.fromIterable(Collections.singletonList(experienciaSofkianoCliente)));

        spy(repository);

        StepVerifier.create(useCase.consultarExperienciasPorCliente(dni))
                .assertNext(experienciaSofkianoClienteDTO -> {
                    assertEquals(STRING_TEST, experienciaSofkianoClienteDTO.descripcion());
                    assertEquals(NIVEL_SATISFACCION, experienciaSofkianoClienteDTO.nivelSatisfaccion());
                    assertEquals(dni, experienciaSofkianoClienteDTO.dniSofkiano());
                    assertEquals(NIT, experienciaSofkianoClienteDTO.nitCliente());
                })
                .verifyComplete();

        verify(repository, times(1))
                .consultarExperienciasPorCliente(anyString());
    }

    @Test
    void consultarExperienciasPorClienteFallidaNoEncontrado() {
        when(repository.consultarExperienciasPorCliente(anyString()))
                .thenReturn(Flux.empty());

        spy(repository);

        StepVerifier.create(useCase.consultarExperienciasPorCliente(dni))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.consultarExperienciasPorCliente(dni))
                .expectErrorMessage(ERROR_EXPERIENCIAS_NO_ENCONTRADAS.getMessage())
                .verify();

        verify(repository, times(2))
                .consultarExperienciasPorCliente(anyString());
    }

    @Test
    void consultarPromedioCalificacionCliente() {
        when(repository.calcularPromedioCliente(anyString(), any(), any()))
                .thenReturn(Mono.just(new PromedioCalificacionCliente(NIT, 5.0)));

        spy(repository);

        StepVerifier.create(useCase.consultarPromedioCalificacionCliente(NIT, LocalDate.now(), LocalDate.now()))
                .assertNext(promedioCalificacionClienteDTO -> {
                    assertEquals(NIT, promedioCalificacionClienteDTO.nitCliente());
                    assertEquals(5.0, promedioCalificacionClienteDTO.promedio());
                })
                .verifyComplete();

        verify(repository, times(1))
                .calcularPromedioCliente(anyString(), any(), any());
    }

    @Test
    void consultarPromedioCalificacionClienteFallidaNoEncontrado() {
        when(repository.calcularPromedioCliente(anyString(), any(), any()))
                .thenReturn(Mono.empty());

        spy(repository);

        StepVerifier.create(useCase.consultarPromedioCalificacionCliente(NIT, LocalDate.now(), LocalDate.now()))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.consultarPromedioCalificacionCliente(NIT, LocalDate.now(), LocalDate.now()))
                .expectErrorMessage(ERROR_EXPERIENCIAS_NO_ENCONTRADAS.getMessage())
                .verify();

        verify(repository, times(2))
                .calcularPromedioCliente(anyString(), any(), any());
    }

    @Test
    void consultarPromedioCalificaciones() {
        when(repository.calcularPromedioCalificaciones(any(), any()))
                .thenReturn(Flux.just(new PromedioCalificacionCliente(NIT, 5.0)));

        spy(repository);

        StepVerifier.create(useCase.consultarPromedioCalificaciones(LocalDate.now(), LocalDate.now()))
                .assertNext(promedioCalificacionClienteDTO -> {
                    assertEquals(NIT, promedioCalificacionClienteDTO.nitCliente());
                    assertEquals(5.0, promedioCalificacionClienteDTO.promedio());
                })
                .verifyComplete();

        verify(repository, times(1))
                .calcularPromedioCalificaciones(any(), any());
    }

    @Test
    void consultarPromedioCalificacionesFallidaNoEncontrado() {
        when(repository.calcularPromedioCalificaciones(any(), any()))
                .thenReturn(Flux.empty());

        spy(repository);

        StepVerifier.create(useCase.consultarPromedioCalificaciones(LocalDate.now(), LocalDate.now()))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.consultarPromedioCalificaciones(LocalDate.now(), LocalDate.now()))
                .expectErrorMessage(ERROR_EXPERIENCIAS_NO_ENCONTRADAS.getMessage())
                .verify();

        verify(repository, times(2))
                .calcularPromedioCalificaciones(any(), any());
    }
}