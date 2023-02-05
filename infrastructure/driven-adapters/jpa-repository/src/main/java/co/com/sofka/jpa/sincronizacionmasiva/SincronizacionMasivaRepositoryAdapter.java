package co.com.sofka.jpa.sincronizacionmasiva;

import co.com.sofka.jpa.helper.AdapterOperations;
import co.com.sofka.jpa.sincronizacionmasiva.mapper.SincronizacionMasivaMapper;
import co.com.sofka.model.sincronizacionmasiva.SincronizacionMasiva;
import co.com.sofka.model.sincronizacionmasiva.gateways.SincronizacionMasivaRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_COMUNICACION_BASE_DATOS;

@Slf4j
@Repository
public class SincronizacionMasivaRepositoryAdapter extends AdapterOperations<SincronizacionMasiva, SincronizacionMasivaData, String, SincronizacionMasivaDataRepository>
 implements SincronizacionMasivaRepository
{

    public SincronizacionMasivaRepositoryAdapter(SincronizacionMasivaDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, SincronizacionMasiva.SincronizacionMasivaBuilder.class).build());
    }

    @Override
    protected SincronizacionMasivaData toData(SincronizacionMasiva entity) {
        return SincronizacionMasivaMapper.toData(entity);
    }

    @Override
    protected SincronizacionMasiva toEntity(SincronizacionMasivaData data) {
        return SincronizacionMasivaMapper.toEntity(data);
    }

    @Override
    public Mono<Boolean> incrementarEjecucionesFallidas(String idSincronizacion) {
        return Mono.fromSupplier( () -> repository.incrementarEjecucionesFallidas(idSincronizacion))
                .subscribeOn(Schedulers.boundedElastic())
                .map( actualizados -> Boolean.TRUE)
                .onErrorResume(throwable -> {
                    log.error(throwable.toString());
                    return Mono.error(ERROR_COMUNICACION_BASE_DATOS::build);
                });
    }

    @Override
    public Mono<Boolean> finalizarSincronizacionMasiva(String idSincronizacion, Integer ejecucionesExitosas) {
        return Mono.fromSupplier(() -> repository.finalizarSincronizacionMasiva(idSincronizacion, ejecucionesExitosas))
                .subscribeOn(Schedulers.boundedElastic())
                .map( actualizados -> Boolean.TRUE)
                .onErrorResume(throwable -> {
                    log.error(throwable.toString());
                    return Mono.error(ERROR_COMUNICACION_BASE_DATOS::build);
                });
    }

    @Override
    public Mono<Boolean> rechazarSincronizacionMasiva(String idSincronizacion) {
        return Mono.fromSupplier(() -> repository.rechazarSincronizacionMasiva(idSincronizacion))
                .subscribeOn(Schedulers.boundedElastic())
                .map( actualizados -> Boolean.TRUE)
                .onErrorResume(throwable -> {
                    log.error(throwable.toString());
                    return Mono.error(ERROR_COMUNICACION_BASE_DATOS::build);
                });
    }
}
