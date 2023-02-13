package co.com.sofka.jwt;


import co.com.sofka.model.token.gateways.TokenGateway;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ACCESO_NO_PERMITIDO;
import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.TOKEN_INVALIDO;

@Component
public class Jwt implements TokenGateway {

    @Value("${app.jwt.secret}")
    private String jwtSigningKey;

    @Override
    public Mono<String> obtenerNombreUsuario(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Mono<Void> validarFechaExpiracion(String token) {
        return obtenerFechaExpiracion(token)
                .flatMap(fecha -> {
                    if (fecha.before(new Date())) {
                        return Mono.error(TOKEN_INVALIDO::build);
                    }
                    return Mono.empty();
                });
    }

    @Override
    public Mono<String> obtenerRol(String token) {
        return Mono.just(token)
                .flatMap(this::extractAllClaims)
                .map(claims -> claims.get("rol", String.class))
                .onErrorResume(throwable -> Mono.error(TOKEN_INVALIDO.build()))
                .switchIfEmpty(Mono.error(ACCESO_NO_PERMITIDO.build()));
    }

    private <T> Mono<T> extractClaim(String token, Function<Claims, T> claimsResolver) {
        return extractAllClaims(token)
                .map(claimsResolver);
    }

    private Mono<Claims> extractAllClaims(String token) {
        return Mono.just(token)
                .map(jwt -> Jwts.parserBuilder()
                        .setSigningKey(getSignInKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody())
                .onErrorResume(throwable -> Mono.error(TOKEN_INVALIDO.build()));
    }

    private Mono<Date> obtenerFechaExpiracion(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(jwtSigningKey.getBytes());
    }

}
