package co.com.sofka.model.experiencia.factoria;

import co.com.sofka.model.exception.negocio.BusinessException;
import co.com.sofka.model.experiencia.ExperienciaSofkianoCliente;
import co.com.sofka.model.experiencia.dto.RegistrarExperienciaSofkianoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.*;
import static org.junit.jupiter.api.Assertions.*;

class ExperienciaSofkianoClienteFactoryTest {

    private static final String DNI = "CC123123";
    private static final String STRING_TEST = "TESTING";
    private static final int NIVEL_SATISFACCION = 10;
    private static final String NIT = "8905625451";

    private RegistrarExperienciaSofkianoDTO registrarExperienciaSofkianoDTO;

    @BeforeEach
    void setUp() {
        registrarExperienciaSofkianoDTO = new RegistrarExperienciaSofkianoDTO(DNI, NIT, NIVEL_SATISFACCION,
                STRING_TEST);
    }

    @Test
    void crearExperienciaSofkianoClienteExitosamente() {
        ExperienciaSofkianoCliente experienciaSofkianoCliente = ExperienciaSofkianoClienteFactory
                .crearExperienciaSofkianoCliente(registrarExperienciaSofkianoDTO);

        assertEquals(DNI, experienciaSofkianoCliente.getSofkiano().getDni());
        assertEquals(NIT, experienciaSofkianoCliente.getCliente().getNit());
        assertEquals(NIVEL_SATISFACCION, experienciaSofkianoCliente.getNivelSatisfaccion());
        assertEquals(STRING_TEST, experienciaSofkianoCliente.getDescripcion());
        assertNotNull(experienciaSofkianoCliente.getFechaCreacion());
        assertNull(experienciaSofkianoCliente.getIdExperiencia());
    }

    @Test
    void crearExperienciaSofkianoClienteFallidaDescripcionLongitudInvalida() {

        registrarExperienciaSofkianoDTO = new RegistrarExperienciaSofkianoDTO(DNI, NIT, NIVEL_SATISFACCION,
                "DESC");

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ExperienciaSofkianoClienteFactory
                        .crearExperienciaSofkianoCliente(registrarExperienciaSofkianoDTO));

        assertEquals(ERROR_LONGITUD_DESCRIPCION.getMessage(), exception.getMessage());
    }

    @Test
    void crearExperienciaSofkianoClienteFallidoFormatoInvalido() {

        registrarExperienciaSofkianoDTO = new RegistrarExperienciaSofkianoDTO(DNI, NIT, NIVEL_SATISFACCION,
                "DESCRIPCION@");

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ExperienciaSofkianoClienteFactory
                        .crearExperienciaSofkianoCliente(registrarExperienciaSofkianoDTO));

        assertEquals(ERROR_FORMATO_DESCRIPCION_INVALIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearExperienciaSofkianoClienteFallidoNivelSatisfaccionMenorCero() {

        registrarExperienciaSofkianoDTO = new RegistrarExperienciaSofkianoDTO(DNI, NIT, -1, STRING_TEST);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ExperienciaSofkianoClienteFactory
                        .crearExperienciaSofkianoCliente(registrarExperienciaSofkianoDTO));

        assertEquals(ERROR_NIVEL_SATISFACCION_VALOR_INVALIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearExperienciaSofkianoClienteFallidoNivelSatisfaccionMayor10() {

        registrarExperienciaSofkianoDTO = new RegistrarExperienciaSofkianoDTO(DNI, NIT, 11, STRING_TEST);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ExperienciaSofkianoClienteFactory
                        .crearExperienciaSofkianoCliente(registrarExperienciaSofkianoDTO));

        assertEquals(ERROR_NIVEL_SATISFACCION_VALOR_INVALIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearExperienciaSofkianoClienteFallidoDNIEsNulo() {

        registrarExperienciaSofkianoDTO = new RegistrarExperienciaSofkianoDTO(null, NIT, NIVEL_SATISFACCION,
                STRING_TEST);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ExperienciaSofkianoClienteFactory
                        .crearExperienciaSofkianoCliente(registrarExperienciaSofkianoDTO));

        assertEquals(ERROR_DNI_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearExperienciaSofkianoClienteFallidoNITEsVacio() {

        registrarExperienciaSofkianoDTO = new RegistrarExperienciaSofkianoDTO(DNI, "", NIVEL_SATISFACCION,
                STRING_TEST);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ExperienciaSofkianoClienteFactory
                        .crearExperienciaSofkianoCliente(registrarExperienciaSofkianoDTO));

        assertEquals(ERROR_NIT_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearExperienciaSofkianoClienteFallidoDescripcionNula() {

        registrarExperienciaSofkianoDTO = new RegistrarExperienciaSofkianoDTO(DNI, NIT, NIVEL_SATISFACCION,
                null);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ExperienciaSofkianoClienteFactory
                        .crearExperienciaSofkianoCliente(registrarExperienciaSofkianoDTO));

        assertEquals(ERROR_DESCRIPCION_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearExperienciaSofkianoClienteFallidoNivelSatisfaccionNulo() {

        registrarExperienciaSofkianoDTO = new RegistrarExperienciaSofkianoDTO(DNI, NIT, null,
                STRING_TEST);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ExperienciaSofkianoClienteFactory
                        .crearExperienciaSofkianoCliente(registrarExperienciaSofkianoDTO));

        assertEquals(ERROR_NIVEL_SATISFACCION_REQUERIDO.getMessage(), exception.getMessage());
    }
}