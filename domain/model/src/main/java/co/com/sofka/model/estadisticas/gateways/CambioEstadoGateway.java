package co.com.sofka.model.estadisticas.gateways;

import co.com.sofka.model.estadisticas.dto.HistorialCambioEstadoDTO;
import reactor.core.publisher.Mono;

public interface CambioEstadoGateway {
    Mono<Boolean> reportarCambioEstadoSofkiano(HistorialCambioEstadoDTO historialCambioEstadoDTO);
}
