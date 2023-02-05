package co.com.sofka.broker.estadisticas.commons.mapper;

import co.com.sofka.broker.estadisticas.commons.dto.ClienteHistorialDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static ClienteHistorialDTO crearClienteDTO(String nit, String razonSocial) {
        return new ClienteHistorialDTO(nit, razonSocial);
    }
}
