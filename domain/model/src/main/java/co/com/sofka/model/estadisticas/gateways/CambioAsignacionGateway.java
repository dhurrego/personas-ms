package co.com.sofka.model.estadisticas.gateways;

import co.com.sofka.model.estadisticas.dto.HistorialAsignacionSofkianoDTO;
import reactor.core.publisher.Mono;

public interface CambioAsignacionGateway {
    Mono<Boolean> reportarCambioAsignacionSofkiano(
            HistorialAsignacionSofkianoDTO historialAsignacionSofkianoDTO);
}
