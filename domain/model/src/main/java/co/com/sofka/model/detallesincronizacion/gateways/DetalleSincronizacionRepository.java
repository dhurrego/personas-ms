package co.com.sofka.model.detallesincronizacion.gateways;

import co.com.sofka.model.detallesincronizacion.DetalleSincronizacion;
import reactor.core.publisher.Mono;

public interface DetalleSincronizacionRepository {
    Mono<DetalleSincronizacion> save(DetalleSincronizacion detalleSincronizacion);
}
