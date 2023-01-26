package co.com.sofka.config;

import org.junit.jupiter.api.Test;
import org.reactivecommons.utils.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

class ObjectMapperConfigTest {

    @Test
    void objectMapper() {
        assertInstanceOf(ObjectMapper.class, new ObjectMapperConfig().objectMapper());
    }
}