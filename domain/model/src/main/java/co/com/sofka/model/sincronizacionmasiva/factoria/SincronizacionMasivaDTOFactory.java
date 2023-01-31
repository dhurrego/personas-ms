package co.com.sofka.model.sincronizacionmasiva.factoria;

import co.com.sofka.model.detallesincronizacion.DetalleSincronizacion;
import co.com.sofka.model.sincronizacionmasiva.SincronizacionMasiva;
import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SincronizacionMasivaDTOFactory {

    public static SincronizacionMasivaDTO crearSincronizacionMasivaDTO(SincronizacionMasiva sincronizacionMasiva) {
        return SincronizacionMasivaDTO.builder()
                .idSincronizacion(sincronizacionMasiva.getIdSincronizacion())
                .estado(sincronizacionMasiva.getEstado())
                .ejecucionesExitosas(sincronizacionMasiva.getEjecucionesExitosas())
                .ejecucionesFallidas(sincronizacionMasiva.getEjecucionesFallidas())
                .detallesSincronizacion(sincronizacionMasiva
                        .getDetallesSincronizacion()
                        .stream()
                        .map(DetalleSincronizacion::getDescripcionFallido)
                        .toList()
                )
                .build();

    }
}
