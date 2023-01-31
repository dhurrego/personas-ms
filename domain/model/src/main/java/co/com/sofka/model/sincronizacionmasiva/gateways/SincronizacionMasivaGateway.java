package co.com.sofka.model.sincronizacionmasiva.gateways;

import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import reactor.core.publisher.Mono;

public interface SincronizacionMasivaGateway {

    Mono<SincronizacionMasivaDTO> solicitarInicioProcesoSincronizacion(SincronizacionMasivaDTO sincronizacionMasivaDTO);
}
