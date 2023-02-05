package co.com.sofka.broker.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class JmsConfigTest {

    @Test
    void messageConverter() {
        JmsConfig jmsConfig = new JmsConfig();
        assertInstanceOf(CustomMessageConverter.class, jmsConfig.messageConverter());
    }
}