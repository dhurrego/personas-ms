package co.com.sofka.model.experiencia;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.sofkiano.Sofkiano;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class ExperienciaSofkianoCliente {
    private Integer idExperiencia;
    private Sofkiano sofkiano;
    private Cliente cliente;
    private Integer nivelSatisfaccion;
    private String descripcion;
    private LocalDateTime fechaCreacion;
}
