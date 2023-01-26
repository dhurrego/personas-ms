package co.com.sofka.model.sofkiano.factoria;

import co.com.sofka.model.exception.negocio.BusinessException;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.dto.SofkianoARegistrarDTO;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.*;
import static org.junit.jupiter.api.Assertions.*;

class SofkianoFactoryTest {

    private static final String TIPO_IDENTIFICACION = "CC";
    private static final String NUMERO_IDENTIFICACION = "1234567890";
    private static final String STRING_TEST = "TEST";

    private SofkianoARegistrarDTO sofkianoARegistrarDTO;

    @Test
    void crearSofkianoExitosamente() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                TIPO_IDENTIFICACION,
                NUMERO_IDENTIFICACION,
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST
        );

        Sofkiano sofkiano = SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO);

        final String DNI = TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION);

        assertEquals(DNI, sofkiano.getDni());
        assertEquals(TIPO_IDENTIFICACION, sofkiano.getTipoIdentificacion().name());
        assertEquals(NUMERO_IDENTIFICACION, sofkiano.getNumeroIdentificacion());
        assertNotNull(sofkiano.getFechaCreacion());
        assertNotNull(sofkiano.getFechaActualizacion());
        assertEquals(Optional.empty(), sofkiano.getFechaSalida());
        assertTrue(sofkiano.isActivo());
    }

    @Test
    void crearSofkianoFallidoPorTipoIdentificacionEsNulo() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                null,
                NUMERO_IDENTIFICACION,
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO));

        assertEquals(ERROR_TIPO_IDENTIFICACION_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearSofkianoFallidoPorTipoIdentificacionEsVacio() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                "",
                NUMERO_IDENTIFICACION,
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO));

        assertEquals(ERROR_TIPO_IDENTIFICACION_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearSofkianoFallidoPorNumeroIdentificacionEsNulo() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                TIPO_IDENTIFICACION,
                null,
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO));

        assertEquals(ERROR_NUMERO_IDENTIFICACION_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearSofkianoFallidoPorPrimerNombreEsNulo() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                TIPO_IDENTIFICACION,
                NUMERO_IDENTIFICACION,
                null,
                Optional.of(STRING_TEST),
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO));

        assertEquals(ERROR_PRIMER_NOMBRE_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearSofkianoFallidoPorPrimerApellidoEsNulo() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                TIPO_IDENTIFICACION,
                NUMERO_IDENTIFICACION,
                STRING_TEST,
                Optional.of(STRING_TEST),
                null,
                Optional.of(STRING_TEST),
                STRING_TEST
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO));

        assertEquals(ERROR_PRIMER_APELLIDO_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearSofkianoFallidoPorDireccionEsNula() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                TIPO_IDENTIFICACION,
                NUMERO_IDENTIFICACION,
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST,
                Optional.of(STRING_TEST),
                null
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO));

        assertEquals(ERROR_DIRECCION_REQUERIDA.getMessage(), exception.getMessage());
    }

    @Test
    void crearSofkianoFallidoPorTipoIdentificacionTieneFormatoInvalido() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                "HI",
                NUMERO_IDENTIFICACION,
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO));

        assertEquals(ERROR_TIPO_IDENTIFICACION_FORMATO_INVALIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearSofkianoFallidoPorNumeroIdentificacionTieneLongitudInvalida() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                TIPO_IDENTIFICACION,
                "12345678901",
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO));

        assertEquals(ERROR_LONGITUD_NUMERO_IDENTIFICACION.getMessage(), exception.getMessage());
    }

    @Test
    void crearSofkianoFallidoPorNumeroIdentificacionFormatoInvalido() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                TIPO_IDENTIFICACION,
                "12345678A0",
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO));

        assertEquals(ERROR_FORMATO_NUMERO_IDENTIFICACION_INVALIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearSofkianoFallidoPorPrimerNombreFormatoInvalido() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                TIPO_IDENTIFICACION,
                NUMERO_IDENTIFICACION,
                "123123",
                Optional.of(STRING_TEST),
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO));

        assertEquals(ERROR_FORMATO_NOMBRE_INVALIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearSofkianoFallidoPorSegundoNombreFormatoInvalido() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                TIPO_IDENTIFICACION,
                NUMERO_IDENTIFICACION,
                STRING_TEST,
                Optional.of("123123"),
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO));

        assertEquals(ERROR_FORMATO_NOMBRE_INVALIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearSofkianoFallidoPorPrimerApellidoFormatoInvalido() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                TIPO_IDENTIFICACION,
                NUMERO_IDENTIFICACION,
                STRING_TEST,
                Optional.of(STRING_TEST),
                "TEST@",
                Optional.of(STRING_TEST),
                STRING_TEST
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO));

        assertEquals(ERROR_FORMATO_NOMBRE_INVALIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearSofkianoFallidoPorSegundoApellidoFormatoInvalido() {
        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                TIPO_IDENTIFICACION,
                NUMERO_IDENTIFICACION,
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST,
                Optional.of("TEST@"),
                STRING_TEST
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> SofkianoFactory.crearSofkiano(sofkianoARegistrarDTO));

        assertEquals(ERROR_FORMATO_NOMBRE_INVALIDO.getMessage(), exception.getMessage());
    }
}