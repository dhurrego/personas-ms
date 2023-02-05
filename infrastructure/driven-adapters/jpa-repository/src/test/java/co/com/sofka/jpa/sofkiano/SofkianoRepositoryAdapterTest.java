package co.com.sofka.jpa.sofkiano;

import co.com.sofka.jpa.sofkiano.data.ConsolidadoAsignacionData;
import co.com.sofka.jpa.sofkiano.data.SofkianoData;
import co.com.sofka.model.exception.tecnico.TechnicalException;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_COMUNICACION_BASE_DATOS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SofkianoRepositoryAdapterTest {
    private static final String TIPO_IDENTIFICACION = "CC";
    private static final String NUMERO_IDENTIFICACION = "1234567890";
    private static final String STRING_TEST = "TEST";

    @InjectMocks
    private SofkianoRepositoryAdapter sofkianoRepositoryAdapter;

    @Mock
    private SofkianoDataRepository repository;

    private Sofkiano sofkiano;
    private SofkianoData sofkianoData;

    @BeforeEach
    void setUp() {
        final String DNI = TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION);

        sofkiano = Sofkiano.builder()
                .dni(DNI)
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

        sofkianoData = SofkianoData.builder()
                .dni(DNI)
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
                .fechaSalida(null)
                .clienteData(null)
                .build();
    }

    @Test
    void obtenerConsolidadoAsignacion() {
        final Long CANTIDAD_CON_ASIGNACION = 1L;
        final Long CANTIDAD_SIN_ASIGNACION = 1L;
        final long TOTAL_SOFKIANOS = CANTIDAD_SIN_ASIGNACION + CANTIDAD_SIN_ASIGNACION;

        ConsolidadoAsignacionData consolidadoConAsignacion = new ConsolidadoAsignacionData();
        consolidadoConAsignacion.setCantidad(CANTIDAD_CON_ASIGNACION);
        consolidadoConAsignacion.setDetalle("CON_ASIGNACION");

        ConsolidadoAsignacionData consolidadoSinAsignacion = new ConsolidadoAsignacionData();
        consolidadoSinAsignacion.setCantidad(CANTIDAD_SIN_ASIGNACION);
        consolidadoSinAsignacion.setDetalle("SIN_ASIGNACION");

        when(repository.obtenerConsolidadoAsignacion())
                .thenReturn(List.of(consolidadoConAsignacion, consolidadoSinAsignacion));

        StepVerifier.create(sofkianoRepositoryAdapter.obtenerConsolidadoAsignacion())
                .assertNext( consolidado -> {
                    assertEquals(CANTIDAD_CON_ASIGNACION.intValue(), consolidado.conAsignacion());
                    assertEquals(CANTIDAD_SIN_ASIGNACION.intValue(), consolidado.sinAsignacion());
                    assertEquals((int) TOTAL_SOFKIANOS, consolidado.totalSofkianos());
                })
                .verifyComplete();
    }

    @Test
    void obtenerConsolidadoAsignacionFallidoPorBD() {
        when(repository.obtenerConsolidadoAsignacion()).thenThrow(new RuntimeException("Desconexion base de datos"));

        StepVerifier.create(sofkianoRepositoryAdapter.obtenerConsolidadoAsignacion())
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(sofkianoRepositoryAdapter.obtenerConsolidadoAsignacion())
                .expectErrorMessage(ERROR_COMUNICACION_BASE_DATOS.getMessage())
                .verify();
    }

    @Test
    void toData() {
        final String DNI = TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION);

        SofkianoData sofkianoData = sofkianoRepositoryAdapter.toData(sofkiano);

        assertEquals(DNI, sofkianoData.getDni());
        assertEquals(TIPO_IDENTIFICACION, sofkianoData.getTipoIdentificacion());
        assertEquals(NUMERO_IDENTIFICACION, sofkianoData.getNumeroIdentificacion());
        assertEquals(STRING_TEST, sofkianoData.getPrimerNombre());
        assertNotNull(sofkianoData.getSegundoNombre());
        assertEquals(STRING_TEST, sofkianoData.getPrimerApellido());
        assertNotNull(sofkianoData.getSegundoApellido());
        assertEquals(STRING_TEST, sofkianoData.getDireccion());
        assertNotNull(sofkianoData.getFechaCreacion());
        assertNotNull(sofkianoData.getFechaActualizacion());
        assertNull(sofkianoData.getFechaSalida());
        assertTrue(sofkianoData.isActivo());
    }

    @Test
    void toEntity() {
        final String DNI = TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION);

        Sofkiano sofkiano = sofkianoRepositoryAdapter.toEntity(sofkianoData);

        assertEquals(DNI, sofkiano.getDni());
        assertEquals(TipoIdentificacion.valueOf(TIPO_IDENTIFICACION), sofkiano.getTipoIdentificacion());
        assertEquals(NUMERO_IDENTIFICACION, sofkiano.getNumeroIdentificacion());
        assertEquals(STRING_TEST, sofkiano.getPrimerNombre());
        assertEquals(Optional.of(STRING_TEST), sofkiano.getSegundoNombre());
        assertEquals(STRING_TEST, sofkiano.getPrimerApellido());
        assertEquals(Optional.of(STRING_TEST), sofkiano.getSegundoApellido());
        assertEquals(STRING_TEST, sofkiano.getDireccion());
        assertNotNull(sofkiano.getFechaCreacion());
        assertNotNull(sofkiano.getFechaActualizacion());
        assertEquals(Optional.empty(), sofkiano.getFechaSalida());
        assertTrue(sofkiano.isActivo());
    }

    @Test
    void findAllExitoso() {
        when(repository.findAll()).thenReturn(Collections.singletonList(sofkianoData));

        StepVerifier.create(sofkianoRepositoryAdapter.findAll())
                .assertNext( sofkiano -> {
                    final String DNI = TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION);

                    assertEquals(DNI, sofkiano.getDni());
                    assertEquals(TipoIdentificacion.valueOf(TIPO_IDENTIFICACION), sofkiano.getTipoIdentificacion());
                    assertEquals(NUMERO_IDENTIFICACION, sofkiano.getNumeroIdentificacion());
                    assertEquals(STRING_TEST, sofkiano.getPrimerNombre());
                    assertEquals(Optional.of(STRING_TEST), sofkiano.getSegundoNombre());
                    assertEquals(STRING_TEST, sofkiano.getPrimerApellido());
                    assertEquals(Optional.of(STRING_TEST), sofkiano.getSegundoApellido());
                    assertEquals(STRING_TEST, sofkiano.getDireccion());
                    assertNotNull(sofkiano.getFechaCreacion());
                    assertNotNull(sofkiano.getFechaActualizacion());
                    assertEquals(Optional.empty(), sofkiano.getFechaSalida());
                    assertTrue(sofkiano.isActivo());
                })
                .verifyComplete();
    }

    @Test
    void findAllOcurrioUnErrorTecnico() {
        when(repository.findAll()).thenThrow(new RuntimeException("Desconexion base de datos"));

        StepVerifier.create(sofkianoRepositoryAdapter.findAll())
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(sofkianoRepositoryAdapter.findAll())
                .expectErrorMessage(ERROR_COMUNICACION_BASE_DATOS.getMessage())
                .verify();
    }

    @Test
    void findByIdExitoso() {
        final String DNI = TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION);

        when(repository.findById(anyString())).thenReturn(Optional.of(sofkianoData));

        StepVerifier.create(sofkianoRepositoryAdapter.findById(DNI))
                .assertNext( sofkiano -> {
                    assertEquals(DNI, sofkiano.getDni());
                    assertEquals(TipoIdentificacion.valueOf(TIPO_IDENTIFICACION), sofkiano.getTipoIdentificacion());
                    assertEquals(NUMERO_IDENTIFICACION, sofkiano.getNumeroIdentificacion());
                    assertEquals(STRING_TEST, sofkiano.getPrimerNombre());
                    assertEquals(Optional.of(STRING_TEST), sofkiano.getSegundoNombre());
                    assertEquals(STRING_TEST, sofkiano.getPrimerApellido());
                    assertEquals(Optional.of(STRING_TEST), sofkiano.getSegundoApellido());
                    assertEquals(STRING_TEST, sofkiano.getDireccion());
                    assertNotNull(sofkiano.getFechaCreacion());
                    assertNotNull(sofkiano.getFechaActualizacion());
                    assertEquals(Optional.empty(), sofkiano.getFechaSalida());
                    assertTrue(sofkiano.isActivo());
                })
                .verifyComplete();
    }

    @Test
    void findByIdOcurrioUnErrorTecnico() {
        final String DNI = TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION);

        when(repository.findById(anyString())).thenThrow(new RuntimeException("Desconexion base de datos"));

        StepVerifier.create(sofkianoRepositoryAdapter.findById(DNI))
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(sofkianoRepositoryAdapter.findById(DNI))
                .expectErrorMessage(ERROR_COMUNICACION_BASE_DATOS.getMessage())
                .verify();
    }

    @Test
    void saveExitoso() {
        when(repository.save(any())).thenReturn(sofkianoData);

        StepVerifier.create(sofkianoRepositoryAdapter.save(sofkiano))
                .assertNext( sofkiano -> {
                    final String DNI = TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION);

                    assertEquals(DNI, sofkiano.getDni());
                    assertEquals(TipoIdentificacion.valueOf(TIPO_IDENTIFICACION), sofkiano.getTipoIdentificacion());
                    assertEquals(NUMERO_IDENTIFICACION, sofkiano.getNumeroIdentificacion());
                    assertEquals(STRING_TEST, sofkiano.getPrimerNombre());
                    assertEquals(Optional.of(STRING_TEST), sofkiano.getSegundoNombre());
                    assertEquals(STRING_TEST, sofkiano.getPrimerApellido());
                    assertEquals(Optional.of(STRING_TEST), sofkiano.getSegundoApellido());
                    assertEquals(STRING_TEST, sofkiano.getDireccion());
                    assertNotNull(sofkiano.getFechaCreacion());
                    assertNotNull(sofkiano.getFechaActualizacion());
                    assertEquals(Optional.empty(), sofkiano.getFechaSalida());
                    assertTrue(sofkiano.isActivo());
                })
                .verifyComplete();
    }

    @Test
    void saveOcurrioUnErrorTecnico() {
        when(repository.save(any())).thenThrow(new RuntimeException("Desconexion base de datos"));

        StepVerifier.create(sofkianoRepositoryAdapter.save(sofkiano))
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(sofkianoRepositoryAdapter.save(sofkiano))
                .expectErrorMessage(ERROR_COMUNICACION_BASE_DATOS.getMessage())
                .verify();
    }
}