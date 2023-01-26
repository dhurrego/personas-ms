package co.com.sofka.model.cliente.factoria;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.cliente.dto.ClienteDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteDTOFactory {

    public static ClienteDTO crearCliente(Cliente cliente) {
        return ClienteDTO.builder()
                .nit(cliente.getNit())
                .razonSocial(cliente.getRazonSocial())
                .build();
    }
}
