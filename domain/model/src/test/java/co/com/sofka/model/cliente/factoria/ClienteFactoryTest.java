package co.com.sofka.model.cliente.factoria;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.cliente.dto.ClienteDTO;
import co.com.sofka.model.exception.negocio.BusinessException;
import org.junit.jupiter.api.Test;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClienteFactoryTest {

    private static final String NIT = "8904789875";
    private static final String RAZON_SOCIAL = "TEST";

    @Test
    void crearClienteExitosamente() {
        ClienteDTO clienteDTO = ClienteDTO.builder()
                .nit(NIT)
                .razonSocial(RAZON_SOCIAL)
                .build();

        Cliente cliente = ClienteFactory.crearCliente(clienteDTO);

        assertEquals(NIT, cliente.getNit());
        assertEquals(RAZON_SOCIAL, cliente.getRazonSocial());
    }

    @Test
    void crearClienteFallidoNitEsNulo() {
        ClienteDTO clienteDTO = ClienteDTO.builder()
                .nit(null)
                .razonSocial(RAZON_SOCIAL)
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ClienteFactory.crearCliente(clienteDTO));

        assertEquals(ERROR_NIT_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearClienteFallidoNitEsVacio() {
        ClienteDTO clienteDTO = ClienteDTO.builder()
                .nit("")
                .razonSocial(RAZON_SOCIAL)
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ClienteFactory.crearCliente(clienteDTO));

        assertEquals(ERROR_NIT_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearClienteFallidoRazonSocialEsNulo() {
        ClienteDTO clienteDTO = ClienteDTO.builder()
                .nit(NIT)
                .razonSocial(null)
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ClienteFactory.crearCliente(clienteDTO));

        assertEquals(ERROR_RAZON_SOCIAL_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearClienteFallidoRazonSocialEsVacio() {
        ClienteDTO clienteDTO = ClienteDTO.builder()
                .nit(NIT)
                .razonSocial("")
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ClienteFactory.crearCliente(clienteDTO));

        assertEquals(ERROR_RAZON_SOCIAL_REQUERIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearClienteFallidoDigitoVerificacionInvalido() {

        ClienteDTO clienteDTO = ClienteDTO.builder()
                .nit("8300402561")
                .razonSocial(RAZON_SOCIAL)
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ClienteFactory.crearCliente(clienteDTO));

        assertEquals(ERROR_DIGITO_VERIFICACION_NIT_INVALIDO.getMessage(), exception.getMessage());
    }

    @Test
    void crearClienteFallidoLongitudNitInvalido() {

        ClienteDTO clienteDTO = ClienteDTO.builder()
                .nit("8905621")
                .razonSocial(RAZON_SOCIAL)
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ClienteFactory.crearCliente(clienteDTO));

        assertEquals(ERROR_LONGITUD_NIT.getMessage(), exception.getMessage());
    }

    @Test
    void crearClienteFallidoFormatoNitInvalido() {

        ClienteDTO clienteDTO = ClienteDTO.builder()
                .nit("890444555A")
                .razonSocial(RAZON_SOCIAL)
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> ClienteFactory.crearCliente(clienteDTO));

        assertEquals(ERROR_FORMATO_NIT_INVALIDO.getMessage(), exception.getMessage());
    }
}