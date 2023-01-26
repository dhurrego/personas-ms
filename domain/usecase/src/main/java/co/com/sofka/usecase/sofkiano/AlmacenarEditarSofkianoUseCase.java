package co.com.sofka.usecase.sofkiano;

import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.dto.SofkianoARegistrarDTO;
import co.com.sofka.model.sofkiano.dto.SofkianoDTO;
import co.com.sofka.model.sofkiano.factoria.SofkianoDTOsFactory;
import co.com.sofka.model.sofkiano.factoria.SofkianoFactory;
import co.com.sofka.model.sofkiano.gateways.SofkianoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_SOFKIANDO_YA_EXISTE;
import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_SOFKIANO_INACTIVO;
import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_SOFKIANO_NO_ENCONTRADO;

@RequiredArgsConstructor
public class AlmacenarEditarSofkianoUseCase {
    private final SofkianoRepository sofkianoRepository;

    public Mono<SofkianoDTO> registrarSofkiano(SofkianoARegistrarDTO sofkianoDTO) {
        return Mono.just(sofkianoDTO.tipoIdentificacion().concat(sofkianoDTO.numeroIdentificacion()))
                .flatMap(sofkianoRepository::findById)
                .doOnNext( sofkiano -> {
                    throw ERROR_SOFKIANDO_YA_EXISTE.build();
                })
                .switchIfEmpty(Mono.just(SofkianoFactory.crearSofkiano(sofkianoDTO)))
                .flatMap(sofkianoRepository::save)
                .map(SofkianoDTOsFactory::crearSofkianoDTO);
    }

    public Mono<SofkianoDTO> actualizarSofkiano(SofkianoARegistrarDTO sofkianoDTO) {
        return Mono.just(sofkianoDTO.tipoIdentificacion().concat(sofkianoDTO.numeroIdentificacion()))
                .flatMap(sofkianoRepository::findById)
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_NO_ENCONTRADO::build))
                .filter(Sofkiano::isActivo)
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_INACTIVO::build))
                .map( sofkianoExistente -> {
                    Sofkiano sofkianoAActualizar = SofkianoFactory.crearSofkiano(sofkianoDTO);
                    sofkianoAActualizar.setFechaCreacion(sofkianoExistente.getFechaCreacion());
                    return sofkianoAActualizar;
                })
                .flatMap(sofkianoRepository::save)
                .map(SofkianoDTOsFactory::crearSofkianoDTO);
    }
}
