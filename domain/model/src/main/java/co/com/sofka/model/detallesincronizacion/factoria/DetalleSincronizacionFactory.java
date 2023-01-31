package co.com.sofka.model.detallesincronizacion.factoria;

import co.com.sofka.model.detallesincronizacion.DetalleSincronizacion;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DetalleSincronizacionFactory {

    public static DetalleSincronizacion crearDetalle(String idSincronizacion, String descripcion) {
        return DetalleSincronizacion.builder()
                .idSincronizacion(idSincronizacion)
                .descripcionFallido(descripcion)
                .build();
    }

}
