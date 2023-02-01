package co.com.sofka.jpa.experiencia;

import co.com.sofka.jpa.experiencia.data.ExperienciaSofkianoClienteData;
import co.com.sofka.jpa.experiencia.mapper.ExperienciaSofkianoClienteMapper;
import co.com.sofka.jpa.helper.AdapterOperations;
import co.com.sofka.model.experiencia.ExperienciaSofkianoCliente;
import co.com.sofka.model.experiencia.PromedioCalificacionCliente;
import co.com.sofka.model.experiencia.gateways.ExperienciaSofkianoClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_COMUNICACION_BASE_DATOS;
import static reactor.core.publisher.Mono.fromSupplier;

@Slf4j
@Repository
public class ExperienciaSofkianoRepositoryAdapter extends AdapterOperations<ExperienciaSofkianoCliente, ExperienciaSofkianoClienteData, Integer, ExperienciaSofkianoClienteDataRepository>
 implements ExperienciaSofkianoClienteRepository
{

    public ExperienciaSofkianoRepositoryAdapter(ExperienciaSofkianoClienteDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, ExperienciaSofkianoCliente.ExperienciaSofkianoClienteBuilder.class).build());
    }

    @Override
    protected ExperienciaSofkianoClienteData toData(ExperienciaSofkianoCliente entity) {
        return ExperienciaSofkianoClienteMapper.toData(entity);
    }

    @Override
    protected ExperienciaSofkianoCliente toEntity(ExperienciaSofkianoClienteData data) {
        return ExperienciaSofkianoClienteMapper.toEntity(data);
    }

    @Override
    public Flux<ExperienciaSofkianoCliente> consultarExperienciasPorSofkiano(String dniSofkiano) {
        return doQueryMany(() -> repository.findBySofkianoDataDniOrderByFechaCreacionDesc(dniSofkiano))
                .onErrorResume(throwable -> Mono.error(ERROR_COMUNICACION_BASE_DATOS::build));
    }

    @Override
    public Flux<ExperienciaSofkianoCliente> consultarExperienciasPorSofkianoYCliente(String dniSofkiano, String nitCliente) {
        return doQueryMany(() ->
                repository.findBySofkianoDataDniAndClienteDataNitOrderByFechaCreacionDesc(dniSofkiano, nitCliente)
        ).onErrorResume(throwable -> Mono.error(ERROR_COMUNICACION_BASE_DATOS::build));
    }

    @Override
    public Flux<ExperienciaSofkianoCliente> consultarExperienciasPorCliente(String nitCliente) {
        return doQueryMany(() -> repository.findByClienteDataNitOrderByFechaCreacionDesc(nitCliente))
                .onErrorResume(throwable -> Mono.error(ERROR_COMUNICACION_BASE_DATOS::build));
    }

    @Override
    public Mono<PromedioCalificacionCliente> calcularPromedioCliente(String nitCliente, LocalDate fechaInicial, LocalDate fechaFinal) {
        return fromSupplier(() -> repository.calcularPromedioCliente(nitCliente, fechaInicial, fechaFinal))
                .subscribeOn(Schedulers.boundedElastic())
                .map( promedioData -> PromedioCalificacionCliente.builder()
                        .nitCliente(promedioData.getNitCliente())
                        .promedio(promedioData.getPromedio())
                        .build())
                .onErrorResume(throwable -> Mono.error(ERROR_COMUNICACION_BASE_DATOS::build));
    }

    @Override
    public Flux<PromedioCalificacionCliente> calcularPromedioCalificaciones(LocalDate fechaInicial, LocalDate fechaFinal) {
        return fromSupplier(() -> repository.calcularPromedioCalificaciones(fechaInicial, fechaFinal))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .map( promedioData -> PromedioCalificacionCliente.builder()
                        .nitCliente(promedioData.getNitCliente())
                        .promedio(promedioData.getPromedio())
                        .build())
                .onErrorResume(throwable -> Mono.error(ERROR_COMUNICACION_BASE_DATOS::build));
    }
}
