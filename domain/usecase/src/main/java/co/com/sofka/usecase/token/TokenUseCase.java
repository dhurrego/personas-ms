package co.com.sofka.usecase.token;

import co.com.sofka.model.token.gateways.TokenGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TokenUseCase {

    private final TokenGateway tokenGateway;

    public Mono<String> obtenerCorreo(String token) {
        return tokenGateway.obtenerNombreUsuario(token);
    }

    public Mono<Void> validarFechaExpiracion(String token) {
        return tokenGateway.validarFechaExpiracion(token);
    }

    public Mono<String> obtenerRol(String token) {
        return tokenGateway.obtenerRol(token);
    }
}
