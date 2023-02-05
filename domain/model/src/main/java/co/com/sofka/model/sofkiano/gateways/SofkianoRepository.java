package co.com.sofka.model.sofkiano.gateways;

import co.com.sofka.model.sofkiano.ConsolidadoAsignacionSofkiano;
import co.com.sofka.model.sofkiano.Sofkiano;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SofkianoRepository {
    Flux<Sofkiano> findAll();
    Mono<Sofkiano> save(Sofkiano sofkiano);
    Mono<Sofkiano> findById(String id);
    Mono<ConsolidadoAsignacionSofkiano> obtenerConsolidadoAsignacion();
}
