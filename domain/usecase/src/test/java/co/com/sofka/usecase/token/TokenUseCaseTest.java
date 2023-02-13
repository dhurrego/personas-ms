package co.com.sofka.usecase.token;

import co.com.sofka.model.token.gateways.TokenGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenUseCaseTest {

    private static final String STRING_TEST = "TEST";

    @InjectMocks
    private TokenUseCase useCase;

    @Mock
    private TokenGateway tokenGateway;

    @Test
    void obtenerCorreo() {
        when(tokenGateway.obtenerNombreUsuario(anyString())).thenReturn(Mono.just(STRING_TEST));
        StepVerifier.create(useCase.obtenerCorreo(STRING_TEST))
                .assertNext(correo -> assertEquals(STRING_TEST, correo))
                .verifyComplete();
    }

    @Test
    void validarFechaExpiracion() {
        when(tokenGateway.validarFechaExpiracion(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(useCase.validarFechaExpiracion(STRING_TEST))
                .verifyComplete();
        verify(tokenGateway, times(1)).validarFechaExpiracion(anyString());
    }

    @Test
    void obtenerRol() {
        when(tokenGateway.obtenerRol(anyString())).thenReturn(Mono.just("ADMINISTRADOR"));
        StepVerifier.create(useCase.obtenerRol(STRING_TEST))
                .assertNext(rol -> assertEquals("ADMINISTRADOR", rol))
                .verifyComplete();
    }
}