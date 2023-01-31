package co.com.sofka.api.sincronizacionmasiva;

import co.com.sofka.api.handler.ResponseExceptionHandler;
import co.com.sofka.model.archivo.dto.ArchivoDTO;
import co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion;
import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import co.com.sofka.usecase.sincronizacionmasiva.ConsultarSincronizacionMasivaUseCase;
import co.com.sofka.usecase.sincronizacionmasiva.SolicitarSincronizacionMasivaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SincronizacionMasivaController.class, ResponseExceptionHandler.class})
@AutoConfigureWebTestClient
@EnableAutoConfiguration
@ExtendWith(MockitoExtension.class)
class SincronizacionMasivaControllerTest {

    private static final String ID_SINCRONIZACION = UUID.randomUUID().toString();

    @MockBean
    private SolicitarSincronizacionMasivaUseCase solicitarSincronizacionMasivaUseCase;

    @MockBean
    private ConsultarSincronizacionMasivaUseCase consultarSincronizacionMasivaUseCase;

    @Autowired
    private WebTestClient webTestClient;

    private SincronizacionMasivaDTO sincronizacionMasivaDTO;

    @BeforeEach
    void setUp() {
        sincronizacionMasivaDTO = SincronizacionMasivaDTO.builder()
                .idSincronizacion(ID_SINCRONIZACION)
                .estado(EstadoSincronizacion.CREADO)
                .ejecucionesExitosas(0)
                .ejecucionesFallidas(0)
                .detallesSincronizacion(Collections.emptyList())
                .build();
    }

    @Test
    void sincronizacionMasivaExitosa() {
        when(solicitarSincronizacionMasivaUseCase.solicitarSincronizacion(any(ArchivoDTO.class)))
                .thenReturn(Mono.just(sincronizacionMasivaDTO));

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file", new ClassPathResource("archivoPrueba.xlsx"))
                .contentType(MediaType.MULTIPART_FORM_DATA);

        webTestClient.post().uri("/sofkianos/sincronizacion-masiva")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBodyList(SincronizacionMasivaDTO.class);
    }

    @Test
    void sincronizacionMasivaMediaTypeInvalido() {
        when(solicitarSincronizacionMasivaUseCase.solicitarSincronizacion(any(ArchivoDTO.class)))
                .thenReturn(Mono.just(sincronizacionMasivaDTO));

        webTestClient.post().uri("/sofkianos/sincronizacion-masiva")
                .body(Mono.just(sincronizacionMasivaDTO), SincronizacionMasivaDTO.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .expectHeader().contentType(APPLICATION_PROBLEM_JSON)
                .expectBodyList(ProblemDetail.class);
    }

    @Test
    void consultarSincronizacionExitosa() {
        when(consultarSincronizacionMasivaUseCase.consultarSincronizacion(anyString()))
                .thenReturn(Mono.just(sincronizacionMasivaDTO));

        webTestClient.get().uri("/sofkianos/sincronizacion-masiva/".concat(ID_SINCRONIZACION))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBodyList(SincronizacionMasivaDTO.class);
    }
}