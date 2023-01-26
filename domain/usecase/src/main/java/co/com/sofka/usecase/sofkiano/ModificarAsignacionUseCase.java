package co.com.sofka.usecase.sofkiano;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.sofkiano.AsignarClienteASofkiano;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.dto.AsignarClienteDTO;
import co.com.sofka.model.sofkiano.factoria.AsignarClienteASofkianoFactory;
import co.com.sofka.model.sofkiano.gateways.SofkianoRepository;
import co.com.sofka.usecase.cliente.ConsultarClientesUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_SOFKIANO_INACTIVO;
import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_SOFKIANO_NO_ENCONTRADO;

@RequiredArgsConstructor
public class ModificarAsignacionUseCase {

    private final SofkianoRepository sofkianoRepository;
    private final ConsultarClientesUseCase consultarClientesUseCase;

    public Mono<String> asignarCliente(AsignarClienteDTO asignarClienteDTO) {
        return Mono.just(AsignarClienteASofkianoFactory.crearAsignarClienteASofkiano(asignarClienteDTO))
                .map(AsignarClienteASofkiano::nitCliente)
                .flatMap(consultarClientesUseCase::listarPorNit)
                .flatMap( cliente -> sofkianoRepository.findById(asignarClienteDTO.dniSofkiano()))
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_NO_ENCONTRADO::build))
                .filter(Sofkiano::isActivo)
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_INACTIVO::build))
                .map( sofkianoExistente ->
                        sofkianoExistente.toBuilder()
                            .cliente(Optional.of(Cliente.builder()
                                    .nit(asignarClienteDTO.nitCliente())
                                    .build()))
                            .fechaActualizacion(LocalDateTime.now())
                            .build())
                .flatMap(sofkianoRepository::save)
                .map( sofkiano -> "Se asignó el cliente correctamente");
    }

    public Mono<String> retirarAsignacion(String dni) {
        return Mono.just(dni)
                .flatMap(sofkianoRepository::findById)
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_NO_ENCONTRADO::build))
                .filter(Sofkiano::isActivo)
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_INACTIVO::build))
                .map( sofkianoExistente ->
                        sofkianoExistente.toBuilder()
                            .cliente(Optional.empty())
                            .fechaActualizacion(LocalDateTime.now())
                            .build())
                .flatMap(sofkianoRepository::save)
                .map( sofkiano -> "Se retiro la asignación del sofkiano");
    }
}
