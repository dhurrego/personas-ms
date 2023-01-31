package co.com.sofka.jpa.sofkiano;

import co.com.sofka.jpa.helper.AdapterOperations;
import co.com.sofka.jpa.sofkiano.data.ConsolidadoAsignacionData;
import co.com.sofka.jpa.sofkiano.data.SofkianoData;
import co.com.sofka.jpa.sofkiano.mapper.SofkianoMapper;
import co.com.sofka.model.sofkiano.ConsolidadoAsignacionSofkiano;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.gateways.SofkianoRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_COMUNICACION_BASE_DATOS;
import static reactor.core.publisher.Mono.fromSupplier;

@Slf4j
@Repository
public class SofkianoRepositoryAdapter extends AdapterOperations<Sofkiano, SofkianoData, String, SofkianoDataRepository>
 implements SofkianoRepository
{

    public SofkianoRepositoryAdapter(SofkianoDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d,Sofkiano.SofkianoBuilder.class).build());
    }

    @Override
    public Mono<ConsolidadoAsignacionSofkiano> obtenerConsolidadoAsignacion() {
        return fromSupplier(repository::obtenerConsolidadoAsignacion)
                .map( consolidado -> {
                    Integer conAsignacion = consolidado.stream()
                            .filter( consolidadoAsignacionData -> "CON_ASIGNACION".equals(consolidadoAsignacionData.getDetalle()))
                            .map(ConsolidadoAsignacionData::getCantidad)
                            .mapToInt(Long::intValue)
                            .sum();
                    Integer sinAsignacion = consolidado.stream()
                            .filter( consolidadoAsignacionData -> "SIN_ASIGNACION".equals(consolidadoAsignacionData.getDetalle()))
                            .map(ConsolidadoAsignacionData::getCantidad)
                            .mapToInt(Long::intValue)
                            .sum();

                    Integer totalSofkianos = conAsignacion + sinAsignacion;

                    return new ConsolidadoAsignacionSofkiano(conAsignacion, sinAsignacion, totalSofkianos);
                }).onErrorResume(throwable -> {
                    log.error(throwable.toString());
                    return Mono.error(ERROR_COMUNICACION_BASE_DATOS::build);
                });
    }

    @Override
    protected SofkianoData toData(Sofkiano entity) {
        return SofkianoMapper.toData(entity);
    }

    @Override
    protected Sofkiano toEntity(SofkianoData data) {
        return SofkianoMapper.toEntity(data);
    }
}
