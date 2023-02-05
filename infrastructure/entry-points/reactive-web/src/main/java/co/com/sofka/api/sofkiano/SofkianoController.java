package co.com.sofka.api.sofkiano;

import co.com.sofka.api.dto.RespuestaGenericaDTO;
import co.com.sofka.model.sofkiano.dto.AsignarClienteDTO;
import co.com.sofka.model.sofkiano.dto.ConsolidadoAsignacionSofkianoDTO;
import co.com.sofka.model.sofkiano.dto.SofkianoARegistrarDTO;
import co.com.sofka.model.sofkiano.dto.SofkianoDTO;
import co.com.sofka.usecase.sofkiano.AlmacenarEditarSofkianoUseCase;
import co.com.sofka.usecase.sofkiano.CambiarEstadoSofkianoUseCase;
import co.com.sofka.usecase.sofkiano.ConsultarSofkianosUseCase;
import co.com.sofka.usecase.sofkiano.ModificarAsignacionUseCase;
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

@RestController
@RequestMapping(value = "/sofkianos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SofkianoController {

    private final ConsultarSofkianosUseCase consultarSofkianosUseCase;
    private final AlmacenarEditarSofkianoUseCase almacenarEditarSofkianoUseCase;
    private final CambiarEstadoSofkianoUseCase cambiarEstadoSofkianoUseCase;
    private final ModificarAsignacionUseCase modificarAsignacionUseCase;

    @Operation(summary = "Servicio para consultar la información de todos los sofkianos. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SofkianoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @GetMapping
    public Mono<ResponseEntity<Flux<SofkianoDTO>>> listarTodos() {
        return Mono.just(ResponseEntity.ok()
                .body(consultarSofkianosUseCase.listarTodos()));
    }

    @Operation(summary = "Servicio para consultar la información de un sofkiano a través del DNI. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SofkianoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @GetMapping("/{dni}")
    public Mono<ResponseEntity<SofkianoDTO>> listarPorDni(@PathVariable String dni) {
        return consultarSofkianosUseCase.listarPorDni(dni).map(ResponseEntity::ok);
    }

    @Operation(summary = "Servicio para consultar la cantidad de sofkianos activos con asignación y sin asignación. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConsolidadoAsignacionSofkianoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @GetMapping("/consolidado-asignacion")
    public Mono<ResponseEntity<ConsolidadoAsignacionSofkianoDTO>> obtenerConsolidadoAsignacion() {
        return consultarSofkianosUseCase.obtenerConsolidadoAsignacion().map(ResponseEntity::ok);
    }

    @Operation(summary = "Servicio para guardar un sofkiano nuevo. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SofkianoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @PostMapping
    public Mono<ResponseEntity<SofkianoDTO>> registrarSofkiano(@RequestBody SofkianoARegistrarDTO sofkianoARegistrarDTO,
                                                               final ServerHttpRequest request) {
        return almacenarEditarSofkianoUseCase.registrarSofkiano(sofkianoARegistrarDTO)
                .map(sofkianoDTO ->
                        ResponseEntity
                                .created(
                                        URI.create(request.getURI()
                                                .toString()
                                                .concat("/")
                                                .concat(
                                                        sofkianoDTO.tipoIdentificacion()
                                                                .concat(sofkianoDTO.numeroIdentificacion()))))
                                .body(sofkianoDTO)
                );
    }

    @Operation(summary = "Servicio para actualizar un sofkiano activo existente. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SofkianoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @PutMapping
    public Mono<ResponseEntity<SofkianoDTO>> actualizarSofkiano(
            @RequestBody SofkianoARegistrarDTO sofkianoARegistrarDTO) {
        return almacenarEditarSofkianoUseCase.actualizarSofkiano(sofkianoARegistrarDTO).map(ResponseEntity::ok);
    }

    @Operation(summary = "Servicio para activar un sofkiano. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RespuestaGenericaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @PatchMapping("/activar/{dni}")
    public Mono<ResponseEntity<RespuestaGenericaDTO>> activarSofkiano(@PathVariable String dni) {
        return cambiarEstadoSofkianoUseCase.activarSofkiano(dni)
                .map(RespuestaGenericaDTO::new)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Servicio para asignar un cliente un sofkiano. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RespuestaGenericaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @PatchMapping("/asignar-cliente")
    public Mono<ResponseEntity<RespuestaGenericaDTO>> asignarCliente(@RequestBody AsignarClienteDTO asignarClienteDTO) {
        return modificarAsignacionUseCase.asignarCliente(asignarClienteDTO)
                .map(RespuestaGenericaDTO::new)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Servicio para retirar la asignación de un cliente a un sofkiano. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RespuestaGenericaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @PatchMapping("/retirar-asignacion/{dni}")
    public Mono<ResponseEntity<RespuestaGenericaDTO>> retirarAsignacion(@PathVariable String dni) {
        return modificarAsignacionUseCase.retirarAsignacion(dni)
                .map(RespuestaGenericaDTO::new)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Servicio para inactivar un sofkiano. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RespuestaGenericaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @DeleteMapping("/inactivar/{dni}")
    public Mono<ResponseEntity<RespuestaGenericaDTO>> inactivarSofkiano(@PathVariable String dni) {
        return cambiarEstadoSofkianoUseCase.inactivarSofkiano(dni)
                .map(RespuestaGenericaDTO::new)
                .map(ResponseEntity::ok);
    }
}
