package co.com.sofka.usecase.sofkiano;

import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.gateways.SofkianoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_SOFKIANO_ACTIVO;
import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_SOFKIANO_INACTIVO;
import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_SOFKIANO_NO_ENCONTRADO;

@RequiredArgsConstructor
public class CambiarEstadoSofkianoUseCase {

    private static final String FORMATO_MENSAJE_INACTIVACION_EXITOSA = "Se inactivo el sofkiano con DNI %s de manera correcta";
    private static final String FORMATO_MENSAJE_ACTIVACION_EXITOSA = "Se activo el sofkiano con DNI %s de manera correcta";
    private final SofkianoRepository sofkianoRepository;

    public Mono<String> inactivarSofkiano(String dni) {
        return Mono.just(dni)
                .flatMap(sofkianoRepository::findById)
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_NO_ENCONTRADO::build))
                .filter(Sofkiano::isActivo)
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_INACTIVO::build))
                .map( sofkianoExistente -> {
                    LocalDateTime fecha = LocalDateTime.now();
                    return sofkianoExistente.toBuilder()
                            .activo(false)
                            .fechaActualizacion(fecha)
                            .fechaSalida(Optional.of(fecha))
                            .build();
                })
                .flatMap(sofkianoRepository::save)
                .map(sofkianoGuardado -> String.format(FORMATO_MENSAJE_INACTIVACION_EXITOSA, sofkianoGuardado.getDni()));
    }

    public Mono<String> activarSofkiano(String dni) {
        return Mono.just(dni)
                .flatMap(sofkianoRepository::findById)
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_NO_ENCONTRADO::build))
                .filter( sofkianoExistente -> !sofkianoExistente.isActivo() )
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_ACTIVO::build))
                .map( sofkianoExistente -> sofkianoExistente.toBuilder()
                            .activo(true)
                            .fechaActualizacion(LocalDateTime.now())
                            .fechaSalida(Optional.empty())
                            .build())
                .flatMap(sofkianoRepository::save)
                .map(sofkianoGuardado -> String.format(FORMATO_MENSAJE_ACTIVACION_EXITOSA, sofkianoGuardado.getDni()));
    }
}
