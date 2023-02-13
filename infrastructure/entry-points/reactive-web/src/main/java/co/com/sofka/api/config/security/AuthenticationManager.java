package co.com.sofka.api.config.security;

import co.com.sofka.usecase.token.TokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.TOKEN_INVALIDO;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenUseCase tokenUseCase;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication.getCredentials())
                .map(Object::toString)
                .doOnNext(tokenUseCase::validarFechaExpiracion)
                .flatMap(token -> tokenUseCase.obtenerCorreo(token)
                        .zipWith(tokenUseCase.obtenerRol(token))
                        .map(tupla -> {
                            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(tupla.getT2());
                            return new UsernamePasswordAuthenticationToken(tupla.getT1(),
                                    null,
                                    Collections.singleton(authority));
                        }))
                .cast(Authentication.class)
                .switchIfEmpty(Mono.error(TOKEN_INVALIDO::build));
    }
}
