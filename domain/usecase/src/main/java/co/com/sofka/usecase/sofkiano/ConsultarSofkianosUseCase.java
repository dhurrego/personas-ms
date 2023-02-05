package co.com.sofka.usecase.sofkiano;

import co.com.sofka.model.sofkiano.dto.ConsolidadoAsignacionSofkianoDTO;
import co.com.sofka.model.sofkiano.dto.SofkianoDTO;
import co.com.sofka.model.sofkiano.factoria.SofkianoDTOsFactory;
import co.com.sofka.model.sofkiano.gateways.SofkianoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_SOFKIANO_NO_ENCONTRADO;

@RequiredArgsConstructor
public class ConsultarSofkianosUseCase {

    private final SofkianoRepository sofkianoRepository;

    public Flux<SofkianoDTO> listarTodos() {
        return sofkianoRepository.findAll()
                .map(SofkianoDTOsFactory::crearSofkianoDTO);
    }

    public Mono<SofkianoDTO> listarPorDni(String dni) {
        return sofkianoRepository.findById(dni)
                .map(SofkianoDTOsFactory::crearSofkianoDTO)
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_NO_ENCONTRADO::build));
    }

    public Mono<ConsolidadoAsignacionSofkianoDTO> obtenerConsolidadoAsignacion() {
        return sofkianoRepository.obtenerConsolidadoAsignacion()
                .map(SofkianoDTOsFactory::crearConsolidadoAsignacionDTO);
    }
}
