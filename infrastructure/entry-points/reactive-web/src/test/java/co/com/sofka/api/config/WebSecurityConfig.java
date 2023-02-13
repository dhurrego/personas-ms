package co.com.sofka.api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ACCESO_NO_AUTORIZADO;
import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ACCESO_NO_PERMITIDO;

@TestConfiguration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((exchange, ex) -> Mono.error(ACCESO_NO_AUTORIZADO::build))
                .accessDeniedHandler((exchange, denied) -> Mono.error(ACCESO_NO_PERMITIDO::build))
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeExchange()
                .anyExchange().permitAll()
                .and()
                .build();
    }
}
