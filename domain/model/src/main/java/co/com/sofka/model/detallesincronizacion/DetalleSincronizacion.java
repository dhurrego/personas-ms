package co.com.sofka.model.detallesincronizacion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class DetalleSincronizacion {
    private Integer idDetalle;
    private String idSincronizacion;
    private String descripcionFallido;
}
