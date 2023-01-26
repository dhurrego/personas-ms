package co.com.sofka.jpa;

import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class TestMainApplication {
    public static void main(String[] args) {
        run(TestMainApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapperImp();
    }
}