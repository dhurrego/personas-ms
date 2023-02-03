package co.com.sofka.broker.estadisticas.historialasignacionsofkiano;

import co.com.sofka.broker.PublicadorMensajes;
import co.com.sofka.broker.estadisticas.historialasignacionsofkiano.dto.AgregarHistorialAsignacionSofkianoDTO;
import co.com.sofka.broker.estadisticas.historialasignacionsofkiano.mapper.HistorialAsignacionMapper;
import co.com.sofka.model.estadisticas.dto.HistorialAsignacionSofkianoDTO;
import co.com.sofka.model.estadisticas.gateways.CambioAsignacionGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class HistorialCambioAsignacionSofkianoAsyncAdapter extends PublicadorMensajes<AgregarHistorialAsignacionSofkianoDTO>
        implements CambioAsignacionGateway {
    private static final String TOPIC_NAME = "actualizacionasignacionsofkiano";

    public HistorialCambioAsignacionSofkianoAsyncAdapter(JmsTemplate jmsTemplate) {
        super(jmsTemplate);
    }

    @Override
    public Mono<Boolean> reportarCambioAsignacionSofkiano(HistorialAsignacionSofkianoDTO historialAsignacion) {
        return Mono.just(historialAsignacion)
                .map(HistorialAsignacionMapper::crearAgregarHistorial)
                .doOnNext( cambioAsignacion -> enviarMensaje(TOPIC_NAME, cambioAsignacion))
                .map( cambioAsignacion -> Boolean.TRUE)
                .onErrorResume( throwable -> {
                    log.error(throwable.toString());
                    return Mono.just(Boolean.TRUE);
                });
    }
}
