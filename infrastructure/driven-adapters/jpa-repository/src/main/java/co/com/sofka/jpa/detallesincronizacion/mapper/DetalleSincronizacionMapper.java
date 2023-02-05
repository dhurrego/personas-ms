package co.com.sofka.jpa.detallesincronizacion.mapper;

import co.com.sofka.jpa.detallesincronizacion.DetalleSincronizacionData;
import co.com.sofka.jpa.sincronizacionmasiva.SincronizacionMasivaData;
import co.com.sofka.model.detallesincronizacion.DetalleSincronizacion;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DetalleSincronizacionMapper {

    public static DetalleSincronizacionData toData(DetalleSincronizacion detalleSincronizacion) {
        return DetalleSincronizacionData.builder()
                .idDetalle(detalleSincronizacion.getIdDetalle())
                .descripcionFallido(detalleSincronizacion.getDescripcionFallido())
                .sincronizacionMasivaData(SincronizacionMasivaData.builder()
                        .idSincronizacion(detalleSincronizacion.getIdSincronizacion())
                        .build())
                .build();
    }

    public static DetalleSincronizacion toEntity(DetalleSincronizacionData detalleSincronizacionData) {
        return DetalleSincronizacion.builder()
                .idDetalle(detalleSincronizacionData.getIdDetalle())
                .descripcionFallido(detalleSincronizacionData.getDescripcionFallido())
                .idSincronizacion(detalleSincronizacionData.getSincronizacionMasivaData().getIdSincronizacion())
                .build();
    }
}
