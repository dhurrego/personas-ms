package co.com.sofka.sb.listener;

import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import co.com.sofka.usecase.sincronizacionmasiva.ProcesarSincronizacionMasivaUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SincronizacionMasivaMessagesListener {

    private static final String TOPIC_NAME = "sincronizacionmasiva";
    private static final String SUBSCRIPTION_NAME = "personas-ms";
    private final ProcesarSincronizacionMasivaUseCase procesarSincronizacionMasivaUseCase;

    @JmsListener(destination = TOPIC_NAME, containerFactory = "topicJmsListenerContainerFactory",
            subscription = SUBSCRIPTION_NAME)
    public void process(SincronizacionMasivaDTO sincronizacionMasivaDTO) {
        log.info("Mensaje recibido: {}", sincronizacionMasivaDTO);
        procesarSincronizacionMasivaUseCase.sincronizar(sincronizacionMasivaDTO)
                .doOnSuccess(unused -> log.info("Se proceso el mensaje exitosamente: {}", sincronizacionMasivaDTO))
                .onErrorResume( throwable -> {
                    log.error("El mensaje se proceso con error: {}", throwable.getMessage());
                    return Mono.empty();
                })
                .block();
    }

}
