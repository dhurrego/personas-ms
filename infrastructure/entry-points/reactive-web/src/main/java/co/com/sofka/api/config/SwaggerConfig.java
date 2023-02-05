package co.com.sofka.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Administracion sofkianos API Documentation" ,
        version = "1.0" ,
        description = "Documentación sobre los servicios utilizados para administrar la información de los sofkianos",
        contact = @Contact( name = "Deivid Urrego",
                email = "deivid.urrego@sofka.com.co",
                url = "https://linkdin.com/in/deivid-urrego")
    )
)
public class SwaggerConfig {

}
