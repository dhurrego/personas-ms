package co.com.sofka.api.experiencia;

import co.com.sofka.api.dto.RespuestaGenericaDTO;
import co.com.sofka.model.experiencia.dto.ExperienciaSofkianoClienteDTO;
import co.com.sofka.model.experiencia.dto.PromedioCalificacionClienteDTO;
import co.com.sofka.model.experiencia.dto.RegistrarExperienciaSofkianoDTO;
import co.com.sofka.usecase.experiencia.ConsultarExperenciasSofkianosUseCase;
import co.com.sofka.usecase.experiencia.RegistrarExperienciaSofkianoClienteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/experiencias-sofkianos-cliente", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ExperienciaSofkianoClienteController {

    private final RegistrarExperienciaSofkianoClienteUseCase registrarExperienciaSofkianoClienteUseCase;
    private final ConsultarExperenciasSofkianosUseCase consultarExperenciasSofkianosUseCase;

    @Operation(summary = "Servicio para consultar todas las experiencias registradas por los sofkianos. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienciaSofkianoClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @GetMapping
    public Mono<ResponseEntity<Flux<ExperienciaSofkianoClienteDTO>>> consultarExperiencias() {
        return Mono.just(ResponseEntity.ok()
                .body(consultarExperenciasSofkianosUseCase.consultarTodasLasExperiencias()));
    }

    @Operation(summary = "Servicio para consultar las experiencias registradas por un sofkiano. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienciaSofkianoClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @GetMapping("/por-sofkiano/{dniSofkiano}")
    public Mono<ResponseEntity<Flux<ExperienciaSofkianoClienteDTO>>> consultarExperienciasPorSofkiano(
            @PathVariable String dniSofkiano
    ) {
        return Mono.just(ResponseEntity.ok()
                .body(consultarExperenciasSofkianosUseCase.consultarExperienciasPorSofkiano(dniSofkiano)));
    }

    @Operation(summary = "Servicio para consultar las experiencias registradas por un sofkiano a un cliente especifico. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienciaSofkianoClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @GetMapping("/por-sofkiano/{dniSofkiano}/{nitCliente}")
    public Mono<ResponseEntity<Flux<ExperienciaSofkianoClienteDTO>>> consultarExperienciasPorSofkianoYCliente(
            @PathVariable String dniSofkiano, @PathVariable String nitCliente
    ) {
        return Mono.just(ResponseEntity.ok()
                .body(consultarExperenciasSofkianosUseCase
                        .consultarExperienciasPorSofkianoYCliente(dniSofkiano, nitCliente)));
    }

    @Operation(summary = "Servicio para consultar las experiencias a un cliente. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienciaSofkianoClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @GetMapping("/por-cliente/{nitCliente}")
    public Mono<ResponseEntity<Flux<ExperienciaSofkianoClienteDTO>>> consultarExperienciasPorCliente(
            @PathVariable String nitCliente
    ) {
        return Mono.just(ResponseEntity.ok()
                .body(consultarExperenciasSofkianosUseCase
                        .consultarExperienciasPorCliente(nitCliente)));
    }

    @Operation(summary = "Servicio para consultar el promedio de calificaciones de un cliente especifico en una fecha especifica. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PromedioCalificacionClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @GetMapping("/promedio-calificaciones-por-cliente")
    public Mono<ResponseEntity<PromedioCalificacionClienteDTO>> consultarPromedioExperienciasPorCliente(
            @RequestParam String nitCliente,
            @RequestParam LocalDate fechaInicial,
            @RequestParam LocalDate fechaFinal
    ) {
        return consultarExperenciasSofkianosUseCase
                .consultarPromedioCalificacionCliente(nitCliente, fechaInicial, fechaFinal)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Servicio para consultar el promedio de calificaciones por cliente en una fecha especifica. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PromedioCalificacionClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @GetMapping("/promedio-calificaciones-clientes")
    public Mono<ResponseEntity<Flux<PromedioCalificacionClienteDTO>>> consultarPromedioExperiencias(
            @RequestParam LocalDate fechaInicial,
            @RequestParam LocalDate fechaFinal
    ) {
        return Mono.just(ResponseEntity.ok()
                .body(consultarExperenciasSofkianosUseCase
                .consultarPromedioCalificaciones(fechaInicial, fechaFinal))
        );
    }

    @Operation(summary = "Servicio para registrar la experiencia de un sofkiano sobre un cliente", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RespuestaGenericaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @PostMapping
    public Mono<ResponseEntity<RespuestaGenericaDTO>> registrarExperiencia(
            @RequestBody RegistrarExperienciaSofkianoDTO registrarExperienciaSofkianoDTO,
            final ServerHttpRequest request) {
        return registrarExperienciaSofkianoClienteUseCase.registrarExperienciaCliente(registrarExperienciaSofkianoDTO)
                .map(RespuestaGenericaDTO::new)
                .map(respuesta ->
                        ResponseEntity
                                .created(
                                        URI.create(request.getURI()
                                                .toString()
                                                .concat("/por-sofkiano/")
                                                .concat(registrarExperienciaSofkianoDTO.dniSofkiano())
                                                .concat("/")
                                                .concat(registrarExperienciaSofkianoDTO.nitCliente())))
                                .body(respuesta));
    }
}
