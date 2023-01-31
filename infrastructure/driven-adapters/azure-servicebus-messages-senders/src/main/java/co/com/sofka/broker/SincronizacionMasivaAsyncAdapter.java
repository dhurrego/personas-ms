package co.com.sofka.broker;

import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import co.com.sofka.model.sincronizacionmasiva.gateways.SincronizacionMasivaGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_PUBLICANDO_MENSAJE_SINCRONIZACION_MASIVA;

@Slf4j
@Component
@RequiredArgsConstructor
public class SincronizacionMasivaAsyncAdapter implements SincronizacionMasivaGateway {

    private static final String TOPIC_NAME = "sincronizacionmasiva";
    private final JmsTemplate jmsTemplate;
    @Override
    public Mono<SincronizacionMasivaDTO> solicitarInicioProcesoSincronizacion(SincronizacionMasivaDTO sincronizacionMasivaDTO) {
        return Mono.just(sincronizacionMasivaDTO)
                .doOnNext( sincronizacionMasiva -> {
                    log.info("Enviando mensaje al topic {}: {}", TOPIC_NAME, sincronizacionMasiva);
                    jmsTemplate.convertAndSend(TOPIC_NAME, sincronizacionMasiva);
                })
                .onErrorResume( throwable -> {
                    log.error(throwable.toString());
                    return Mono.error(ERROR_PUBLICANDO_MENSAJE_SINCRONIZACION_MASIVA.build());
                });
    }
}
