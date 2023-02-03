package co.com.sofka.broker.estadisticas.historialcambioestado;

import co.com.sofka.broker.PublicadorMensajes;
import co.com.sofka.broker.estadisticas.historialcambioestado.dto.AgregarHistorialCambioEstadoDTO;
import co.com.sofka.broker.estadisticas.historialcambioestado.mapper.HistorialCambioEstadoMapper;
import co.com.sofka.model.estadisticas.dto.HistorialCambioEstadoDTO;
import co.com.sofka.model.estadisticas.gateways.CambioEstadoGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class HistorialCambioEstadoSofkianoAsyncAdapter extends PublicadorMensajes<AgregarHistorialCambioEstadoDTO>
        implements CambioEstadoGateway {

    private static final String TOPIC_NAME = "cambioestadosofkiano";

    public HistorialCambioEstadoSofkianoAsyncAdapter(JmsTemplate jmsTemplate) {
        super(jmsTemplate);
    }

    @Override
    public Mono<Boolean> reportarCambioEstadoSofkiano(
            HistorialCambioEstadoDTO historialCambioEstadoDTO) {
        return Mono.just(historialCambioEstadoDTO)
                .map(HistorialCambioEstadoMapper::crearAgregarHistorial)
                .doOnNext( cambioEstado -> enviarMensaje(TOPIC_NAME, cambioEstado))
                .map( cambioEstado -> Boolean.TRUE)
                .onErrorResume( throwable -> {
                    log.error(throwable.toString());
                    return Mono.just(Boolean.TRUE);
                });
    }
}
