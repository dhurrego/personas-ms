package co.com.sofka.model.sincronizacionmasiva.factoria;

import co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion;
import co.com.sofka.model.sincronizacionmasiva.SincronizacionMasiva;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SincronizacionMasivaFactory {

    public static SincronizacionMasiva crearSincronizacionMasiva(String idSincronizacion) {
        return SincronizacionMasiva.builder()
                .idSincronizacion(idSincronizacion)
                .estado(EstadoSincronizacion.CREADO)
                .ejecucionesExitosas(0)
                .ejecucionesFallidas(0)
                .detallesSincronizacion(Collections.emptyList())
                .build();
    }
}
