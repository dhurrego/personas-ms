package co.com.sofka.model.sofkiano;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder(toBuilder = true)
public class Sofkiano {
    private String dni;
    private TipoIdentificacion tipoIdentificacion;
    private String numeroIdentificacion;
    private String primerNombre;
    private Optional<String> segundoNombre;
    private String primerApellido;
    private Optional<String> segundoApellido;
    private String direccion;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Optional<LocalDateTime> fechaSalida;
    private Optional<Cliente> cliente;
}
