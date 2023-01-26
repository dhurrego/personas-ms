package co.com.sofka.jpa.cliente;

import co.com.sofka.model.cliente.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ClienteRepositoryAdapterTest {

    private static final String NIT = "8905625551";
    private static final String RAZON_SOCIAL = "TEST";

    @InjectMocks
    private ClienteRepositoryAdapter clienteRepositoryAdapter;

    @Test
    void toData() {
        Cliente cliente = Cliente.builder()
                .nit(NIT)
                .razonSocial(RAZON_SOCIAL)
                .build();

        ClienteData clienteData = clienteRepositoryAdapter.toData(cliente);

        assertEquals(NIT, clienteData.getNit());
        assertEquals(RAZON_SOCIAL, clienteData.getRazonSocial());
    }

    @Test
    void toEntity() {
        ClienteData clienteData = ClienteData.builder()
                .nit(NIT)
                .razonSocial(RAZON_SOCIAL)
                .build();

        Cliente cliente = clienteRepositoryAdapter.toEntity(clienteData);

        assertEquals(NIT, cliente.getNit());
        assertEquals(RAZON_SOCIAL, cliente.getRazonSocial());
    }
}