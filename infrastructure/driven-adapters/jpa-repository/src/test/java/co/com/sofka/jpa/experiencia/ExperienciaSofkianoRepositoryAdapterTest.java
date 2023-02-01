package co.com.sofka.jpa.experiencia;

import co.com.sofka.jpa.cliente.ClienteData;
import co.com.sofka.jpa.experiencia.data.ExperienciaSofkianoClienteData;
import co.com.sofka.jpa.experiencia.data.PromedioExperienciaClienteData;
import co.com.sofka.jpa.sofkiano.data.SofkianoData;
import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.exception.tecnico.TechnicalException;
import co.com.sofka.model.experiencia.ExperienciaSofkianoCliente;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_COMUNICACION_BASE_DATOS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExperienciaSofkianoRepositoryAdapterTest {

    private static final Integer ID_EXPERIENCIA = 1;
    private static final String STRING_TEST = "TEST";
    private static final String DNI = "CC123123123";
    private static final String TIPO_IDENTIFICACION = "CC";
    private static final String NUMERO_IDENTIFICACION = "123123123";
    private static final int NIVEL_SATISFACCION = 10;
    private static final String NIT = "8905625451";

    @InjectMocks
    private ExperienciaSofkianoRepositoryAdapter experienciaSofkianoRepositoryAdapter;

    @Mock
    private ExperienciaSofkianoClienteDataRepository repository;

    private Sofkiano sofkiano;
    private Cliente cliente;

    private SofkianoData sofkianoData;
    private ClienteData clienteData;

    private ExperienciaSofkianoCliente experienciaSofkianoCliente;
    private ExperienciaSofkianoClienteData experienciaSofkianoClienteData;

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

        cliente = Cliente.builder()
                .nit(NIT)
                .razonSocial(STRING_TEST)
                .build();

        sofkianoData = SofkianoData.builder()
                .dni(TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION))
                .tipoIdentificacion(TIPO_IDENTIFICACION)
                .numeroIdentificacion(NUMERO_IDENTIFICACION)
                .primerNombre(STRING_TEST)
                .segundoNombre(STRING_TEST)
                .primerApellido(STRING_TEST)
                .segundoApellido(STRING_TEST)
                .activo(true)
                .direccion(STRING_TEST)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        clienteData = ClienteData.builder()
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


        experienciaSofkianoClienteData = ExperienciaSofkianoClienteData.builder()
                .idExperiencia(ID_EXPERIENCIA)
                .descripcion(STRING_TEST)
                .fechaCreacion(LocalDateTime.now())
                .sofkianoData(sofkianoData)
                .clienteData(clienteData)
                .nivelSatisfaccion(NIVEL_SATISFACCION)
                .build();
    }

    @Test
    void toData() {
        ExperienciaSofkianoClienteData data = experienciaSofkianoRepositoryAdapter.toData(experienciaSofkianoCliente);

        assertEquals(ID_EXPERIENCIA, data.getIdExperiencia());
        assertEquals(STRING_TEST, data.getDescripcion());
        assertEquals(NIVEL_SATISFACCION, data.getNivelSatisfaccion());
        assertEquals(sofkiano.getDni(), data.getSofkianoData().getDni());
        assertEquals(cliente.getNit(), data.getClienteData().getNit());
    }

    @Test
    void toEntity() {
        ExperienciaSofkianoCliente entity = experienciaSofkianoRepositoryAdapter.toEntity(experienciaSofkianoClienteData);

        assertEquals(ID_EXPERIENCIA, entity.getIdExperiencia());
        assertEquals(STRING_TEST, entity.getDescripcion());
        assertEquals(NIVEL_SATISFACCION, entity.getNivelSatisfaccion());
        assertEquals(sofkianoData.getDni(), entity.getSofkiano().getDni());
        assertEquals(clienteData.getNit(), entity.getCliente().getNit());
    }

    @Test
    void consultarExperienciasPorSofkianoExitoso() {
        when(repository.findBySofkianoDataDniOrderByFechaCreacionDesc(anyString()))
                .thenReturn(List.of(experienciaSofkianoClienteData));

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.consultarExperienciasPorSofkiano(DNI))
                .assertNext( experienciaSofkianoCliente -> {
                    assertEquals(ID_EXPERIENCIA, experienciaSofkianoCliente.getIdExperiencia());
                    assertEquals(STRING_TEST, experienciaSofkianoCliente.getDescripcion());
                    assertEquals(NIVEL_SATISFACCION, experienciaSofkianoCliente.getNivelSatisfaccion());
                    assertEquals(sofkianoData.getDni(), experienciaSofkianoCliente.getSofkiano().getDni());
                    assertEquals(clienteData.getNit(), experienciaSofkianoCliente.getCliente().getNit());
                })
                .verifyComplete();
    }

    @Test
    void consultarExperienciasPorSofkianoFallidoPorBD() {
        when(repository.findBySofkianoDataDniOrderByFechaCreacionDesc(anyString()))
                .thenThrow(new RuntimeException("Desconexion base de datos"));

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.consultarExperienciasPorSofkiano(DNI))
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.consultarExperienciasPorSofkiano(DNI))
                .expectErrorMessage(ERROR_COMUNICACION_BASE_DATOS.getMessage())
                .verify();
    }

    @Test
    void consultarExperienciasPorSofkianoYClienteExitoso() {
        when(repository.findBySofkianoDataDniAndClienteDataNitOrderByFechaCreacionDesc(anyString(), anyString()))
                .thenReturn(List.of(experienciaSofkianoClienteData));

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.consultarExperienciasPorSofkianoYCliente(DNI, NIT))
                .assertNext( experienciaSofkianoCliente -> {
                    assertEquals(ID_EXPERIENCIA, experienciaSofkianoCliente.getIdExperiencia());
                    assertEquals(STRING_TEST, experienciaSofkianoCliente.getDescripcion());
                    assertEquals(NIVEL_SATISFACCION, experienciaSofkianoCliente.getNivelSatisfaccion());
                    assertEquals(sofkianoData.getDni(), experienciaSofkianoCliente.getSofkiano().getDni());
                    assertEquals(clienteData.getNit(), experienciaSofkianoCliente.getCliente().getNit());
                })
                .verifyComplete();
    }

    @Test
    void consultarExperienciasPorSofkianoYClienteFallidoPorBD() {
        when(repository.findBySofkianoDataDniAndClienteDataNitOrderByFechaCreacionDesc(anyString(), anyString()))
                .thenThrow(new RuntimeException("Desconexion base de datos"));

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.consultarExperienciasPorSofkianoYCliente(DNI, NIT))
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.consultarExperienciasPorSofkianoYCliente(DNI, NIT))
                .expectErrorMessage(ERROR_COMUNICACION_BASE_DATOS.getMessage())
                .verify();
    }

    @Test
    void consultarExperienciasPorClienteExitoso() {
        when(repository.findByClienteDataNitOrderByFechaCreacionDesc(anyString()))
                .thenReturn(List.of(experienciaSofkianoClienteData));

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.consultarExperienciasPorCliente(NIT))
                .assertNext( experienciaSofkianoCliente -> {
                    assertEquals(ID_EXPERIENCIA, experienciaSofkianoCliente.getIdExperiencia());
                    assertEquals(STRING_TEST, experienciaSofkianoCliente.getDescripcion());
                    assertEquals(NIVEL_SATISFACCION, experienciaSofkianoCliente.getNivelSatisfaccion());
                    assertEquals(sofkianoData.getDni(), experienciaSofkianoCliente.getSofkiano().getDni());
                    assertEquals(clienteData.getNit(), experienciaSofkianoCliente.getCliente().getNit());
                })
                .verifyComplete();
    }

    @Test
    void consultarExperienciasPorClienteFallidoPorBD() {
        when(repository.findByClienteDataNitOrderByFechaCreacionDesc(anyString()))
                .thenThrow(new RuntimeException("Desconexion base de datos"));

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.consultarExperienciasPorCliente(NIT))
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.consultarExperienciasPorCliente(NIT))
                .expectErrorMessage(ERROR_COMUNICACION_BASE_DATOS.getMessage())
                .verify();
    }

    @Test
    void calcularPromedioClienteExitoso() {
        PromedioExperienciaClienteData promedioExperienciaClienteData = new PromedioExperienciaClienteData(NIT, 5.0);

        when(repository.calcularPromedioCliente(anyString(), any(), any()))
                .thenReturn(promedioExperienciaClienteData);

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.calcularPromedioCliente(NIT, LocalDate.now(),
                        LocalDate.now()))
                .assertNext( promedioCliente -> {
                    assertEquals(NIT, promedioCliente.nitCliente());
                    assertEquals(5.0, promedioCliente.promedio());
                })
                .verifyComplete();
    }

    @Test
    void calcularPromedioClienteFallidoPorBD() {
        when(repository.calcularPromedioCliente(anyString(), any(), any()))
                .thenThrow(new RuntimeException("Desconexion base de datos"));

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.calcularPromedioCliente(NIT, LocalDate.now(),
                        LocalDate.now()))
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.calcularPromedioCliente(NIT, LocalDate.now(),
                        LocalDate.now()))
                .expectErrorMessage(ERROR_COMUNICACION_BASE_DATOS.getMessage())
                .verify();
    }

    @Test
    void calcularPromedioCalificacionesExitoso() {
        PromedioExperienciaClienteData promedioExperienciaClienteData = new PromedioExperienciaClienteData(NIT, 5.0);

        when(repository.calcularPromedioCalificaciones(any(), any()))
                .thenReturn(List.of(promedioExperienciaClienteData));

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.calcularPromedioCalificaciones(LocalDate.now(),
                        LocalDate.now()))
                .assertNext( promedioCliente -> {
                    assertEquals(NIT, promedioCliente.nitCliente());
                    assertEquals(5.0, promedioCliente.promedio());
                })
                .verifyComplete();
    }

    @Test
    void calcularPromedioCalificacionesFallidoPorBD() {
        when(repository.calcularPromedioCalificaciones(any(), any()))
                .thenThrow(new RuntimeException("Desconexion base de datos"));

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.calcularPromedioCalificaciones(LocalDate.now(),
                        LocalDate.now()))
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(experienciaSofkianoRepositoryAdapter.calcularPromedioCalificaciones(LocalDate.now(),
                        LocalDate.now()))
                .expectErrorMessage(ERROR_COMUNICACION_BASE_DATOS.getMessage())
                .verify();
    }
}