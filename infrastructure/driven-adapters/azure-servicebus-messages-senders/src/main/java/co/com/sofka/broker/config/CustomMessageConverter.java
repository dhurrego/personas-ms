package co.com.sofka.broker.config;

import co.com.sofka.broker.estadisticas.historialasignacionsofkiano.dto.AgregarHistorialAsignacionSofkianoDTO;
import co.com.sofka.broker.estadisticas.historialcambioestado.dto.AgregarHistorialCambioEstadoDTO;
import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.jms.BytesMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import org.apache.qpid.jms.message.JmsBytesMessage;
import org.apache.qpid.jms.provider.amqp.message.AmqpJmsMessageFacade;
import org.apache.qpid.proton.amqp.Symbol;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomMessageConverter extends MappingJackson2MessageConverter {

    private static final String ID_PROPIEDAD_DEFECTO = "_type";
    private static final String ID_PROPIEDAD_CARGA_MASIVA = "_type_sincronizacion_masiva";
    private static final String ID_PROPIEDAD_CAMBIO_ESTADO = "_type_cambio_estado";
    private static final String ID_PROPIEDAD_ACTUALIZACION_ASIGNACION = "_type_actualizacion_asignacion";
    private static final Symbol CONTENT_TYPE = Symbol.valueOf("application/json");

    public CustomMessageConverter() {
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put(ID_PROPIEDAD_CARGA_MASIVA, SincronizacionMasivaDTO.class);
        typeIdMappings.put(ID_PROPIEDAD_CAMBIO_ESTADO, AgregarHistorialCambioEstadoDTO.class);
        typeIdMappings.put(ID_PROPIEDAD_ACTUALIZACION_ASIGNACION, AgregarHistorialAsignacionSofkianoDTO.class);

        this.setTypeIdMappings(typeIdMappings);
        this.setTargetType(MessageType.BYTES);
        this.setTypeIdPropertyName(ID_PROPIEDAD_DEFECTO);
    }

    @Override
    protected BytesMessage mapToBytesMessage(Object object, Session session, ObjectWriter objectWriter)
        throws JMSException, IOException {
        final BytesMessage bytesMessage = super.mapToBytesMessage(object, session, objectWriter);
        JmsBytesMessage jmsBytesMessage = (JmsBytesMessage) bytesMessage;
        AmqpJmsMessageFacade facade = (AmqpJmsMessageFacade) jmsBytesMessage.getFacade();
        facade.setContentType(CONTENT_TYPE);
        return jmsBytesMessage;
    }
}