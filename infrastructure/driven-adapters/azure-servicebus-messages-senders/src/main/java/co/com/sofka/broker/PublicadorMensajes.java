package co.com.sofka.broker;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;

@RequiredArgsConstructor
public abstract class PublicadorMensajes<T> {

    private final JmsTemplate jmsTemplate;

    protected void enviarMensaje(String nombreDestino, T mensaje) {
        jmsTemplate.convertAndSend(nombreDestino, mensaje);
    }
}
