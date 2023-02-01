package co.com.sofka.api.experiencia;

import co.com.sofka.api.dto.RespuestaGenericaDTO;
import co.com.sofka.api.handler.ResponseExceptionHandler;
import co.com.sofka.model.experiencia.dto.ExperienciaSofkianoClienteDTO;
import co.com.sofka.model.experiencia.dto.PromedioCalificacionClienteDTO;
import co.com.sofka.model.experiencia.dto.RegistrarExperienciaSofkianoDTO;
import co.com.sofka.usecase.experiencia.ConsultarExperenciasSofkianosUseCase;
import co.com.sofka.usecase.experiencia.RegistrarExperienciaSofkianoClienteUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ExperienciaSofkianoClienteController.class, ResponseExceptionHandler.class})
@AutoConfigureWebTestClient
@EnableAutoConfiguration
@ExtendWith(MockitoExtension.class)
class ExperienciaSofkianoClienteControllerTest {

    private static final String DNI_SOFKIANO = "CC123123";
    public static final String NIT_CLIENTE = "890562312";
    public static final String FECHA = "2023-01-01";

    @MockBean
    private RegistrarExperienciaSofkianoClienteUseCase registrarExperienciaSofkianoClienteUseCase;

    @MockBean
    private ConsultarExperenciasSofkianosUseCase consultarExperenciasSofkianosUseCase;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void consultarExperiencias() {
        when(consultarExperenciasSofkianosUseCase.consultarTodasLasExperiencias()).thenReturn(Flux.empty());

        webTestClient.get().uri("/experiencias-sofkianos-cliente")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBodyList(ExperienciaSofkianoClienteDTO.class);
    }

    @Test
    void consultarExperienciasPorSofkiano() {
        when(consultarExperenciasSofkianosUseCase.consultarExperienciasPorSofkiano(anyString()))
                .thenReturn(Flux.empty());

        webTestClient.get().uri("/experiencias-sofkianos-cliente/por-sofkiano/".concat(DNI_SOFKIANO))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBodyList(ExperienciaSofkianoClienteDTO.class);
    }

    @Test
    void consultarExperienciasPorSofkianoYCliente() {
        when(consultarExperenciasSofkianosUseCase.consultarExperienciasPorSofkianoYCliente(anyString(), anyString()))
                .thenReturn(Flux.empty());

        webTestClient.get().uri("/experiencias-sofkianos-cliente/por-sofkiano/".concat(DNI_SOFKIANO)
                        .concat("/").concat(NIT_CLIENTE))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBodyList(ExperienciaSofkianoClienteDTO.class);
    }

    @Test
    void consultarExperienciasPorCliente() {
        when(consultarExperenciasSofkianosUseCase.consultarExperienciasPorCliente(anyString()))
                .thenReturn(Flux.empty());

        webTestClient.get().uri("/experiencias-sofkianos-cliente/por-cliente/".concat(NIT_CLIENTE))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBodyList(ExperienciaSofkianoClienteDTO.class);
    }

    @Test
    void consultarPromedioExperienciasPorCliente() {
        when(consultarExperenciasSofkianosUseCase.consultarPromedioCalificacionCliente(anyString(),
                any(LocalDate.class), any(LocalDate.class))
        ).thenReturn(Mono.just(PromedioCalificacionClienteDTO.builder()
                .nitCliente(NIT_CLIENTE)
                .promedio(0.0D)
                .build()
        ));

        webTestClient.get().uri( uriBuilder ->
                        uriBuilder.path("/experiencias-sofkianos-cliente/promedio-calificaciones-por-cliente")
                                .queryParam("nitCliente", NIT_CLIENTE)
                                .queryParam("fechaInicial", FECHA)
                                .queryParam("fechaFinal", FECHA)
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody(PromedioCalificacionClienteDTO.class);
    }

    @Test
    void consultarPromedioExperiencias() {
        when(consultarExperenciasSofkianosUseCase.consultarPromedioCalificaciones(any(LocalDate.class),
                any(LocalDate.class))
        ).thenReturn(Flux.just(new PromedioCalificacionClienteDTO(NIT_CLIENTE, 0.0D)));

        webTestClient.get().uri( uriBuilder ->
                        uriBuilder.path("/experiencias-sofkianos-cliente/promedio-calificaciones-clientes")
                                .queryParam("fechaInicial", FECHA)
                                .queryParam("fechaFinal", FECHA)
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBodyList(PromedioCalificacionClienteDTO.class);
    }

    @Test
    void registrarExperiencia() {
        when(registrarExperienciaSofkianoClienteUseCase.registrarExperienciaCliente(
                any(RegistrarExperienciaSofkianoDTO.class)))
                .thenReturn(Mono.just("OK"));

        RegistrarExperienciaSofkianoDTO registrarExperienciaSofkianoDTO =
                new RegistrarExperienciaSofkianoDTO(DNI_SOFKIANO, NIT_CLIENTE, 10, "OK");

        webTestClient.post().uri("/experiencias-sofkianos-cliente")
                .body(Mono.just(registrarExperienciaSofkianoDTO), RegistrarExperienciaSofkianoDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody(RespuestaGenericaDTO.class);
    }
}