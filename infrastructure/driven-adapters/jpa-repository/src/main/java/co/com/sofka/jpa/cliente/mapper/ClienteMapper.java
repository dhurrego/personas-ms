package co.com.sofka.jpa.cliente.mapper;

import co.com.sofka.jpa.cliente.ClienteData;
import co.com.sofka.model.cliente.Cliente;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toEntity(ClienteData clienteData) {
        if(Objects.isNull(clienteData)) {
            return null;
        }

        return Cliente.builder()
                .nit(clienteData.getNit())
                .razonSocial(clienteData.getRazonSocial())
                .build();
    }

    public static ClienteData toData(Cliente cliente) {
        if(Objects.isNull(cliente)) {
            return null;
        }

        return ClienteData.builder()
                .nit(cliente.getNit())
                .razonSocial(cliente.getRazonSocial())
                .build();
    }
}
