package co.com.sofka.jpa.detallesincronizacion;

import co.com.sofka.jpa.detallesincronizacion.mapper.DetalleSincronizacionMapper;
import co.com.sofka.jpa.helper.AdapterOperations;
import co.com.sofka.model.detallesincronizacion.DetalleSincronizacion;
import co.com.sofka.model.detallesincronizacion.gateways.DetalleSincronizacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class DetalleSincronizacionRepositoryAdapter extends AdapterOperations<DetalleSincronizacion, DetalleSincronizacionData, Integer, DetalleSincronizacionDataRepository>
 implements DetalleSincronizacionRepository
{

    public DetalleSincronizacionRepositoryAdapter(DetalleSincronizacionDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, DetalleSincronizacion.DetalleSincronizacionBuilder.class).build());
    }

    @Override
    protected DetalleSincronizacionData toData(DetalleSincronizacion entity) {
        return DetalleSincronizacionMapper.toData(entity);
    }

    @Override
    protected DetalleSincronizacion toEntity(DetalleSincronizacionData data) {
        return DetalleSincronizacionMapper.toEntity(data);
    }
}
