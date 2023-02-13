package co.com.sofka.api.sofkiano;

import co.com.sofka.api.config.WebSecurityConfig;
import co.com.sofka.api.dto.RespuestaGenericaDTO;
import co.com.sofka.api.handler.ReactiveExceptionHandler;
import co.com.sofka.model.sofkiano.dto.AsignarClienteDTO;
import co.com.sofka.model.sofkiano.dto.ConsolidadoAsignacionSofkianoDTO;
import co.com.sofka.model.sofkiano.dto.SofkianoARegistrarDTO;
import co.com.sofka.model.sofkiano.dto.SofkianoDTO;
import co.com.sofka.usecase.sofkiano.AlmacenarEditarSofkianoUseCase;
import co.com.sofka.usecase.sofkiano.CambiarEstadoSofkianoUseCase;
import co.com.sofka.usecase.sofkiano.ConsultarSofkianosUseCase;
import co.com.sofka.usecase.sofkiano.ModificarAsignacionUseCase;
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

import java.util.Optional;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.*;
import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_COMUNICACION_BASE_DATOS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SofkianoController.class, ReactiveExceptionHandler.class})
@Import(WebSecurityConfig.class)
@AutoConfigureWebTestClient
@EnableAutoConfiguration
@ExtendWith(MockitoExtension.class)
class SofkianoControllerTest {

    private static final String DNI = "CC123123123";
    private static final String TIPO_IDENTIFICACION = "CC";
    private static final String NUMERO_IDENTIFICACION = "123123123";
    private static final String STRING_TEST = "TEST";
    private static final String NIT = "8905652352";

    @MockBean
    private ConsultarSofkianosUseCase consultarSofkianosUseCase;

    @MockBean
    private AlmacenarEditarSofkianoUseCase almacenarEditarSofkianoUseCase;

    @MockBean
    private CambiarEstadoSofkianoUseCase cambiarEstadoSofkianoUseCase;

    @MockBean
    private ModificarAsignacionUseCase modificarAsignacionUseCase;

    @Autowired
    private WebTestClient webTestClient;

    private SofkianoDTO sofkianoDTO;
    private ConsolidadoAsignacionSofkianoDTO consolidadoAsignacionSofkianoDTO;
    private SofkianoARegistrarDTO sofkianoARegistrarDTO;
    private AsignarClienteDTO asignarClienteDTO;

    @BeforeEach
    void setUp() {
        sofkianoDTO = SofkianoDTO.builder()
                .tipoIdentificacion(TIPO_IDENTIFICACION)
                .numeroIdentificacion(NUMERO_IDENTIFICACION)
                .primerNombre(STRING_TEST)
                .primerApellido(STRING_TEST)
                .activo(true)
                .direccion(STRING_TEST)
                .build();

        consolidadoAsignacionSofkianoDTO = new ConsolidadoAsignacionSofkianoDTO(0, 0, 0);

        sofkianoARegistrarDTO = new SofkianoARegistrarDTO(
                TIPO_IDENTIFICACION,
                NUMERO_IDENTIFICACION,
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST,
                Optional.of(STRING_TEST),
                STRING_TEST);

        asignarClienteDTO = new AsignarClienteDTO(DNI, NIT);
    }

    @Test
    void listarTodosExitoso() {
        when(consultarSofkianosUseCase.listarTodos()).thenReturn(Flux.empty());

        webTestClient.get().uri("/sofkianos")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBodyList(SofkianoDTO.class);
    }

    @Test
    void listarPorDniExitoso() {
        when(consultarSofkianosUseCase.listarPorDni(anyString()))
                .thenReturn(Mono.just(sofkianoDTO));

        webTestClient.get().uri("/sofkianos/".concat(DNI))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody(SofkianoDTO.class);
    }

    @Test
    void listarPorDniFallidoPorqueNoFueEncontrado() {
        when(consultarSofkianosUseCase.listarPorDni(anyString()))
                .thenThrow(ERROR_SOFKIANO_NO_ENCONTRADO.build());

        webTestClient.get().uri("/sofkianos/".concat(DNI))
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class);
    }

    @Test
    void obtenerConsolidadoAsignacionExitosamente() {
        when(consultarSofkianosUseCase.obtenerConsolidadoAsignacion())
                .thenReturn(Mono.just(consolidadoAsignacionSofkianoDTO));

        webTestClient.get().uri("/sofkianos/consolidado-asignacion")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody(ConsolidadoAsignacionSofkianoDTO.class);
    }

    @Test
    void registrarSofkianoExitosamente() {
        when(almacenarEditarSofkianoUseCase.registrarSofkiano(any(SofkianoARegistrarDTO.class)))
                .thenReturn(Mono.just(sofkianoDTO));

        webTestClient.post()
                .uri("/sofkianos")
                .body(Mono.just(sofkianoARegistrarDTO), SofkianoARegistrarDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody(SofkianoDTO.class);
    }

    @Test
    void registrarSofkianoFallidoPorPeticionInvalida() {
        when(almacenarEditarSofkianoUseCase.registrarSofkiano(any(SofkianoARegistrarDTO.class)))
                .thenThrow(ERROR_DIRECCION_REQUERIDA.build());

        webTestClient.post()
                .uri("/sofkianos")
                .body(Mono.just(sofkianoARegistrarDTO), SofkianoARegistrarDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class);
    }

    @Test
    void actualizarSofkianoExitosamente() {
        when(almacenarEditarSofkianoUseCase.actualizarSofkiano(any(SofkianoARegistrarDTO.class)))
                .thenReturn(Mono.just(sofkianoDTO));

        webTestClient.put()
                .uri("/sofkianos")
                .body(Mono.just(sofkianoARegistrarDTO), SofkianoARegistrarDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody(SofkianoDTO.class);
    }

    @Test
    void actualizarSofkianoFallidoSofkianoNoExiste() {
        when(almacenarEditarSofkianoUseCase.actualizarSofkiano(any(SofkianoARegistrarDTO.class)))
                .thenThrow(ERROR_SOFKIANO_NO_ENCONTRADO.build());

        webTestClient.put()
                .uri("/sofkianos")
                .body(Mono.just(sofkianoARegistrarDTO), SofkianoARegistrarDTO.class)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class);
    }

    @Test
    void activarSofkianoExitosamente() {
        when(cambiarEstadoSofkianoUseCase.activarSofkiano(anyString()))
                .thenReturn(Mono.just("Sofkiano activado!"));

        webTestClient.patch()
                .uri("/sofkianos/activar/".concat(DNI))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody(RespuestaGenericaDTO.class);
    }

    @Test
    void activarSofkianoFallidoPorqueYaEstaActivo() {
        when(cambiarEstadoSofkianoUseCase.activarSofkiano(anyString()))
                .thenThrow(ERROR_SOFKIANO_ACTIVO.build());

        webTestClient.patch()
                .uri("/sofkianos/activar/".concat(DNI))
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class);
    }

    @Test
    void asignarClienteExitosamente() {
        when(modificarAsignacionUseCase.asignarCliente(any(AsignarClienteDTO.class)))
                .thenReturn(Mono.just("Cliente asignado al sofkiano correctamente"));

        webTestClient.patch()
                .uri("/sofkianos/asignar-cliente")
                .body(Mono.just(asignarClienteDTO), AsignarClienteDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody(RespuestaGenericaDTO.class);
    }

    @Test
    void asignarClienteFallidoRequestInvalido() {
        when(modificarAsignacionUseCase.asignarCliente(any(AsignarClienteDTO.class)))
                .thenThrow(ERROR_DNI_REQUERIDO.build());

        webTestClient.patch()
                .uri("/sofkianos/asignar-cliente")
                .body(Mono.just(asignarClienteDTO), AsignarClienteDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class);
    }

    @Test
    void retirarAsignacionExitosamente() {
        when(modificarAsignacionUseCase.retirarAsignacion(anyString()))
                .thenReturn(Mono.just("Se retiro la asignación sofkiano correctamente"));

        webTestClient.patch()
                .uri("/sofkianos/retirar-asignacion/".concat(DNI))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody(RespuestaGenericaDTO.class);
    }

    @Test
    void inactivarSofkianoExitosamente() {
        when(cambiarEstadoSofkianoUseCase.inactivarSofkiano(anyString()))
                .thenReturn(Mono.just("Se inactivo al sofkiano correctamente"));

        webTestClient.delete()
                .uri("/sofkianos/inactivar/".concat(DNI))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody(RespuestaGenericaDTO.class);
    }

    @Test
    void validarControlMetodoNoPermitido() {
        webTestClient.delete()
                .uri("/sofkianos/activar/".concat(DNI))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class);
    }

    @Test
    void validarFormatoRequestInvalido() {
        webTestClient.post()
                .uri("/sofkianos")
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class);
    }

    @Test
    void validarControlErrorTecnico() {
        when(cambiarEstadoSofkianoUseCase.activarSofkiano(anyString()))
                .thenThrow(ERROR_COMUNICACION_BASE_DATOS.build());

        webTestClient.patch()
                .uri("/sofkianos/activar/".concat(DNI))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class);
    }

    @Test
    void validarControlCualquierExcepcion() {
        when(cambiarEstadoSofkianoUseCase.activarSofkiano(anyString()))
                .thenThrow(new RuntimeException("Ocurrio un error"));

        webTestClient.patch()
                .uri("/sofkianos/activar/".concat(DNI))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBody(ProblemDetail.class);
    }
}