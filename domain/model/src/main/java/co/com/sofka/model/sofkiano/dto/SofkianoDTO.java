package co.com.sofka.model.sofkiano.dto;

import co.com.sofka.model.cliente.dto.ClienteDTO;
import lombok.Builder;

import java.io.Serializable;
import java.util.Optional;

@Builder()
public record SofkianoDTO(
        String tipoIdentificacion,
        String numeroIdentificacion,
        String primerNombre,
        Optional<String>segundoNombre,
        String primerApellido,
        Optional<String> segundoApellido,
        String direccion,
        boolean activo,
        Optional<ClienteDTO> cliente
)  implements Serializable {
}
