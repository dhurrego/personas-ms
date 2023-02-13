package co.com.sofka.api.cliente;

import co.com.sofka.api.config.WebSecurityConfig;
import co.com.sofka.api.handler.ReactiveExceptionHandler;
import co.com.sofka.model.cliente.dto.ClienteDTO;
import co.com.sofka.usecase.cliente.AlmacenarEditarClienteUseCase;
import co.com.sofka.usecase.cliente.ConsultarClientesUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_CLIENTE_NO_ENCONTRADO;
import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_FORMATO_NIT_INVALIDO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ClienteController.class, ReactiveExceptionHandler.class})
@Import(WebSecurityConfig.class)
@AutoConfigureWebTestClient
@EnableAutoConfiguration
@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    private static final String STRING_TEST = "TEST";
    private static final String NIT = "8905652352";

    @MockBean
    private ConsultarClientesUseCase consultarClientesUseCase;

    @MockBean
    private AlmacenarEditarClienteUseCase almacenarEditarClienteUseCase;

    @Autowired
    private WebTestClient webTestClient;

    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        clienteDTO = ClienteDTO.builder()
                .nit(NIT)
                .razonSocial(STRING_TEST)
                .build();
    }

    @Test
    void listarTodosExitoso() {
        when(consultarClientesUseCase.listarTodos()).thenReturn(Flux.empty());

        webTestClient.get().uri("/clientes")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBodyList(ClienteDTO.class);
    }

    @Test
    void listarPorNit() {
        when(consultarClientesUseCase.listarPorNit(anyString()))
                .thenReturn(Mono.just(clienteDTO));

        webTestClient.get().uri("/clientes/".concat(NIT))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody(ClienteDTO.class);
    }

    @Test
    void listarPorNitFallidoPorqueNoFueEncontrado() {
        when(consultarClientesUseCase.listarPorNit(anyString()))
                .thenThrow(ERROR_CLIENTE_NO_ENCONTRADO.build());

        webTestClient.get().uri("/clientes/".concat(NIT))
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class);
    }

    @Test
    void registrarClienteExitosamente() {
        when(almacenarEditarClienteUseCase.registrarCliente(any(ClienteDTO.class)))
                .thenReturn(Mono.just(clienteDTO));

        webTestClient.post()
                .uri("/clientes")
                .body(Mono.just(clienteDTO), ClienteDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody(ClienteDTO.class);
    }

    @Test
    void registrarClienteFallidoPorPeticionInvalida() {
        when(almacenarEditarClienteUseCase.registrarCliente(any(ClienteDTO.class)))
                .thenThrow(ERROR_FORMATO_NIT_INVALIDO.build());

        webTestClient.post()
                .uri("/clientes")
                .body(Mono.just(clienteDTO), ClienteDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class);
    }

    @Test
    void actualizarSofkianoExitosamente() {
        when(almacenarEditarClienteUseCase.actualizarCliente(any(ClienteDTO.class)))
                .thenReturn(Mono.just(clienteDTO));

        webTestClient.put()
                .uri("/clientes")
                .body(Mono.just(clienteDTO), ClienteDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody(ClienteDTO.class);
    }

    @Test
    void actualizarClienteFallidoClienteNoExiste() {
        when(almacenarEditarClienteUseCase.actualizarCliente(any(ClienteDTO.class)))
                .thenThrow(ERROR_CLIENTE_NO_ENCONTRADO.build());

        webTestClient.put()
                .uri("/clientes")
                .body(Mono.just(clienteDTO), ClienteDTO.class)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class);
    }
}