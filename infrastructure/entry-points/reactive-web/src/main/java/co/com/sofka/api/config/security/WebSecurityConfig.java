package co.com.sofka.api.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ACCESO_NO_AUTORIZADO;
import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ACCESO_NO_PERMITIDO;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final ReactiveAuthenticationManager authenticationManager;
    private final ServerSecurityContextRepository securityContextRepository;

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
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/swagger-doc/swagger-resources/**").permitAll()
                .pathMatchers("/swagger-doc/swagger-ui.html").permitAll()
                .pathMatchers("/swagger-doc/documentacion/swagger-ui/**").permitAll()
                .pathMatchers("/swagger-doc/v3/api-docs").permitAll()
                .pathMatchers("/swagger-doc/v3/api-docs/**").permitAll()
                .pathMatchers("/swagger-doc/documentacion/webjars/**").permitAll()
                .anyExchange().hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                .and()
                .build();
    }
}
