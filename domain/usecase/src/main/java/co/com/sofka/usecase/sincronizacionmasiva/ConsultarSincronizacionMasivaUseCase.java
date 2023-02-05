package co.com.sofka.usecase.sincronizacionmasiva;

import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import co.com.sofka.model.sincronizacionmasiva.factoria.SincronizacionMasivaDTOFactory;
import co.com.sofka.model.sincronizacionmasiva.gateways.SincronizacionMasivaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_PROCESO_SINCRONIZACION_MASIVA_NO_ENCONTRADO;

@RequiredArgsConstructor
public class ConsultarSincronizacionMasivaUseCase {

    private final SincronizacionMasivaRepository sincronizacionMasivaRepository;

    public Flux<SincronizacionMasivaDTO> consultarTodas() {
        return sincronizacionMasivaRepository.findAll()
                .map(SincronizacionMasivaDTOFactory::crearSincronizacionMasivaDTO);
    }

    public Mono<SincronizacionMasivaDTO> consultarSincronizacion(String idSincronizacion) {
        return sincronizacionMasivaRepository.findById(idSincronizacion)
                .map(SincronizacionMasivaDTOFactory::crearSincronizacionMasivaDTO)
                .switchIfEmpty(Mono.error(ERROR_PROCESO_SINCRONIZACION_MASIVA_NO_ENCONTRADO::build));
    }
}
