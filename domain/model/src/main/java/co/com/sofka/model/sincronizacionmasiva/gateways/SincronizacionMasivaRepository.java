package co.com.sofka.model.sincronizacionmasiva.gateways;

import co.com.sofka.model.sincronizacionmasiva.SincronizacionMasiva;
import reactor.core.publisher.Mono;

public interface SincronizacionMasivaRepository {
    Mono<SincronizacionMasiva> save(SincronizacionMasiva sincronizacionMasiva);
    Mono<SincronizacionMasiva> findById(String idSincronizacion);
    Mono<Boolean> incrementarEjecucionesFallidas(String idSincronizacion);
    Mono<Boolean> finalizarSincronizacionMasiva(String idSincronizacion, Integer ejecucionesExitosas);
    Mono<Boolean> rechazarSincronizacionMasiva(String idSincronizacion);
}
