package co.com.sofka.usecase.experiencia;

import co.com.sofka.model.experiencia.dto.ExperienciaSofkianoClienteDTO;
import co.com.sofka.model.experiencia.dto.PromedioCalificacionClienteDTO;
import co.com.sofka.model.experiencia.factoria.ExperienciaSofkianoClienteDTOFactory;
import co.com.sofka.model.experiencia.gateways.ExperienciaSofkianoClienteRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_EXPERIENCIAS_NO_ENCONTRADAS;

@RequiredArgsConstructor
public class ConsultarExperenciasSofkianosUseCase {

    private final ExperienciaSofkianoClienteRepository repository;

    public Flux<ExperienciaSofkianoClienteDTO> consultarTodasLasExperiencias() {
        return repository.findAll()
                .map(ExperienciaSofkianoClienteDTOFactory::crearExperienciaSofkianoClienteDTO);
    }

    public Flux<ExperienciaSofkianoClienteDTO> consultarExperienciasPorSofkiano(String dniSofkiano) {
        return repository.consultarExperienciasPorSofkiano(dniSofkiano)
                .switchIfEmpty(Mono.error(ERROR_EXPERIENCIAS_NO_ENCONTRADAS::build))
                .map(ExperienciaSofkianoClienteDTOFactory::crearExperienciaSofkianoClienteDTO);
    }

    public Flux<ExperienciaSofkianoClienteDTO> consultarExperienciasPorSofkianoYCliente(String dniSofkiano,
                                                                                        String nitCliente) {
        return repository.consultarExperienciasPorSofkianoYCliente(dniSofkiano, nitCliente)
                .switchIfEmpty(Mono.error(ERROR_EXPERIENCIAS_NO_ENCONTRADAS::build))
                .map(ExperienciaSofkianoClienteDTOFactory::crearExperienciaSofkianoClienteDTO);
    }

    public Flux<ExperienciaSofkianoClienteDTO> consultarExperienciasPorCliente(String nitCliente) {
        return repository.consultarExperienciasPorCliente(nitCliente)
                .switchIfEmpty(Mono.error(ERROR_EXPERIENCIAS_NO_ENCONTRADAS::build))
                .map(ExperienciaSofkianoClienteDTOFactory::crearExperienciaSofkianoClienteDTO);
    }

    public Mono<PromedioCalificacionClienteDTO> consultarPromedioCalificacionCliente(String nitCliente,
                                                                                     LocalDate fechaInicial,
                                                                                     LocalDate fechaFinal) {
        return repository.calcularPromedioCliente(nitCliente, fechaInicial, fechaFinal)
                .switchIfEmpty(Mono.error(ERROR_EXPERIENCIAS_NO_ENCONTRADAS::build))
                .map( promedioCliente ->
                        PromedioCalificacionClienteDTO.builder()
                                .nitCliente(promedioCliente.nitCliente())
                                .promedio(promedioCliente.promedio())
                                .build());
    }

    public Flux<PromedioCalificacionClienteDTO> consultarPromedioCalificaciones(LocalDate fechaInicial,
                                                                                LocalDate fechaFinal) {
        return repository.calcularPromedioCalificaciones(fechaInicial, fechaFinal)
                .switchIfEmpty(Mono.error(ERROR_EXPERIENCIAS_NO_ENCONTRADAS::build))
                .map( promedioCliente ->
                        PromedioCalificacionClienteDTO.builder()
                                .nitCliente(promedioCliente.nitCliente())
                                .promedio(promedioCliente.promedio())
                                .build());
    }
}
