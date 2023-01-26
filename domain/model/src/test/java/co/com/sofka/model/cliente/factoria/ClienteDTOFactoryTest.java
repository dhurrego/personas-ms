package co.com.sofka.model.cliente.factoria;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.cliente.dto.ClienteDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClienteDTOFactoryTest {

    private static final String NIT = "8904789875";
    private static final String RAZON_SOCIAL = "TEST";

    @Test
    void crearClienteExitosamente() {
        Cliente cliente = Cliente.builder()
                .nit(NIT)
                .razonSocial(RAZON_SOCIAL)
                .build();

        ClienteDTO clienteDTO = ClienteDTOFactory.crearCliente(cliente);

        assertEquals(NIT, clienteDTO.nit());
        assertEquals(RAZON_SOCIAL, clienteDTO.razonSocial());
    }
}