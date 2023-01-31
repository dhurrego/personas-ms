package co.com.sofka.jpa.sincronizacionmasiva.mapper;

import co.com.sofka.jpa.detallesincronizacion.DetalleSincronizacionData;
import co.com.sofka.jpa.detallesincronizacion.mapper.DetalleSincronizacionMapper;
import co.com.sofka.jpa.sincronizacionmasiva.SincronizacionMasivaData;
import co.com.sofka.model.detallesincronizacion.DetalleSincronizacion;
import co.com.sofka.model.sincronizacionmasiva.SincronizacionMasiva;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SincronizacionMasivaMapper {

    public static SincronizacionMasivaData toData(SincronizacionMasiva sincronizacionMasiva) {

        List<DetalleSincronizacionData> detalleData = new ArrayList<>();

        if (Objects.nonNull(sincronizacionMasiva.getDetallesSincronizacion())) {
            detalleData = sincronizacionMasiva
                    .getDetallesSincronizacion()
                    .stream()
                    .map(DetalleSincronizacionMapper::toData)
                    .toList();
        }

        return SincronizacionMasivaData.builder()
                .idSincronizacion(sincronizacionMasiva.getIdSincronizacion())
                .ejecucionesExitosas(sincronizacionMasiva.getEjecucionesExitosas())
                .ejecucionesFallidas(sincronizacionMasiva.getEjecucionesFallidas())
                .estado(sincronizacionMasiva.getEstado())
                .detallesSincronizacionData(detalleData)
                .build();
    }

    public static SincronizacionMasiva toEntity(SincronizacionMasivaData sincronizacionMasivaData) {

        List<DetalleSincronizacion> detalle = new ArrayList<>();

        if (Objects.nonNull(sincronizacionMasivaData.getDetallesSincronizacionData())) {
            detalle = sincronizacionMasivaData
                    .getDetallesSincronizacionData()
                    .stream()
                    .map(DetalleSincronizacionMapper::toEntity)
                    .toList();
        }

        return SincronizacionMasiva.builder()
                .idSincronizacion(sincronizacionMasivaData.getIdSincronizacion())
                .ejecucionesExitosas(sincronizacionMasivaData.getEjecucionesExitosas())
                .ejecucionesFallidas(sincronizacionMasivaData.getEjecucionesFallidas())
                .estado(sincronizacionMasivaData.getEstado())
                .detallesSincronizacion(detalle)
                .build();
    }
}
