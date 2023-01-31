package co.com.sofka.model.sincronizacionmasiva;

import co.com.sofka.model.detallesincronizacion.DetalleSincronizacion;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class SincronizacionMasiva {
    private String idSincronizacion;
    private EstadoSincronizacion estado;
    private Integer ejecucionesExitosas;
    private Integer ejecucionesFallidas;
    private List<DetalleSincronizacion> detallesSincronizacion;
}
