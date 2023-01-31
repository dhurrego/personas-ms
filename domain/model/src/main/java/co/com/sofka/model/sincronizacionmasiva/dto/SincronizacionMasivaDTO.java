package co.com.sofka.model.sincronizacionmasiva.dto;

import co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public record SincronizacionMasivaDTO(
        String idSincronizacion,
        EstadoSincronizacion estado,
        Integer ejecucionesExitosas,
        Integer ejecucionesFallidas,
        List<String> detallesSincronizacion
) implements Serializable { }
