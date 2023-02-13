package co.com.sofka.jwt;

import co.com.sofka.model.exception.tecnico.TechnicalException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.test.StepVerifier;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.TOKEN_INVALIDO;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@SpringBootConfiguration
@ExtendWith(MockitoExtension.class)
class JwtTest {

    private static final String CORREO = "correo@correo.com.co";
    private static final String ROL = "ADMINISTRADOR";

    @InjectMocks
    private Jwt jwt;

    @Value("${app.jwt.secret}")
    private String jwtSigningKey;
    private Integer expiration = 24;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwt, "jwtSigningKey", jwtSigningKey);
    }

    @Test
    void obtenerNombreUsuario() {
        String token = this.crearToken();

        StepVerifier.create(jwt.obtenerNombreUsuario(token))
                .assertNext(correo -> assertEquals(CORREO, correo))
                .verifyComplete();
    }

    @Test
    void validarFechaExpiracion() {
        String token = this.crearToken();

        StepVerifier.create(jwt.validarFechaExpiracion(token))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void validarFechaExpiracionFallidoExpirado() {
        this.expiration = 0;
        String token = this.crearToken();

        StepVerifier.create(jwt.validarFechaExpiracion(token))
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(jwt.validarFechaExpiracion(token))
                .expectErrorMessage(TOKEN_INVALIDO.getMessage())
                .verify();
    }

    @Test
    void obtenerRol() {
        String token = this.crearToken();

        StepVerifier.create(jwt.obtenerRol(token))
                .assertNext(rol -> assertEquals(ROL, rol))
                .verifyComplete();
    }

    private String crearToken() {
        Key key = Keys.hmacShaKeyFor(jwtSigningKey.getBytes());

        return Jwts
                .builder()
                .setSubject(CORREO)
                .claim("rol", ROL)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(expiration)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}