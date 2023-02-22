package co.com.sofka.model.token.gateways;

import reactor.core.publisher.Mono;

public interface TokenGateway {
    Mono<String> obtenerNombreUsuario(String token);
    Mono<Void> validarFechaExpiracion(String token);
    Mono<String> obtenerRol(String token);
}
