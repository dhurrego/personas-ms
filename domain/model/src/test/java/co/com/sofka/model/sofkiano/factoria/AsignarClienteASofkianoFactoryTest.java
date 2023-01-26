package co.com.sofka.model.sofkiano.factoria;

import co.com.sofka.model.exception.negocio.BusinessException;
import co.com.sofka.model.sofkiano.AsignarClienteASofkiano;
import co.com.sofka.model.sofkiano.dto.AsignarClienteDTO;
import org.junit.jupiter.api.Test;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_DNI_REQUERIDO;
import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_NIT_REQUERIDO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AsignarClienteASofkianoFactoryTest {

    private static final String NIT_CLIENTE = "8905256581";
    private static final String DNI_SOFKIANO = "CC123123";

    @Test
    void crearAsignarClienteASofkianoExitoso() {

        AsignarClienteDTO asignarClienteDTO = new AsignarClienteDTO(DNI_SOFKIANO, NIT_CLIENTE);

        AsignarClienteASofkiano asignarClienteASofkiano = AsignarClienteASofkianoFactory
                .crearAsignarClienteASofkiano(asignarClienteDTO);

        assertEquals(DNI_SOFKIANO, asignarClienteASofkiano.dniSofkiano());
        assertEquals(NIT_CLIENTE, asignarClienteASofkiano.nitCliente());
    }

    @Test
    void crearAsignarClienteASofkianoFallidoDniSofkianoEsNulo() {
        AsignarClienteDTO asignarClienteDTO = new AsignarClienteDTO(null, NIT_CLIENTE);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> AsignarClienteASofkianoFactory.crearAsignarClienteASofkiano(asignarClienteDTO));

        assertEquals(ERROR_DNI_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearAsignarClienteASofkianoFallidoNitClienteEsNulo() {
        AsignarClienteDTO asignarClienteDTO = new AsignarClienteDTO(DNI_SOFKIANO, null);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> AsignarClienteASofkianoFactory.crearAsignarClienteASofkiano(asignarClienteDTO));

        assertEquals(ERROR_NIT_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearAsignarClienteASofkianoFallidoDniSofkianoEsVacio() {
        AsignarClienteDTO asignarClienteDTO = new AsignarClienteDTO("", NIT_CLIENTE);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> AsignarClienteASofkianoFactory.crearAsignarClienteASofkiano(asignarClienteDTO));

        assertEquals(ERROR_DNI_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearAsignarClienteASofkianoFallidoNitClienteEsVacio() {
        AsignarClienteDTO asignarClienteDTO = new AsignarClienteDTO(DNI_SOFKIANO, "");

        BusinessException exception = assertThrows(BusinessException.class,
                () -> AsignarClienteASofkianoFactory.crearAsignarClienteASofkiano(asignarClienteDTO));

        assertEquals(ERROR_NIT_REQUERIDO.getMessage(), exception.getMessage());
    }
}