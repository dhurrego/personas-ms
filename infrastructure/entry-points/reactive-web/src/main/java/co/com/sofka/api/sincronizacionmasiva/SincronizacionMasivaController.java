package co.com.sofka.api.sincronizacionmasiva;

import co.com.sofka.api.sincronizacionmasiva.mapper.ArchivoMapper;
import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import co.com.sofka.usecase.sincronizacionmasiva.ConsultarSincronizacionMasivaUseCase;
import co.com.sofka.usecase.sincronizacionmasiva.SolicitarSincronizacionMasivaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(value = "/sincronizaciones-masivas", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SincronizacionMasivaController {

    private final SolicitarSincronizacionMasivaUseCase solicitarSincronizacionMasivaUseCase;
    private final ConsultarSincronizacionMasivaUseCase consultarSincronizacionMasivaUseCase;

    @Operation(summary = "Servicio para consultar todas las sincronizaciones masivas realizadas", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SincronizacionMasivaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @GetMapping()
    public Mono<ResponseEntity<Flux<SincronizacionMasivaDTO>>> consultarTodas() {
        return Mono.just(ResponseEntity.ok()
                .body(consultarSincronizacionMasivaUseCase.consultarTodas()));
    }

    @Operation(summary = "Servicio para consultar una sincronizacion masiva especifica", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SincronizacionMasivaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @GetMapping("/{idSincronizacion}")
    public Mono<ResponseEntity<SincronizacionMasivaDTO>> consultarSincronizacion(@PathVariable String idSincronizacion) {
        return consultarSincronizacionMasivaUseCase.consultarSincronizacion(idSincronizacion)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Servicio para realizar una sincronizacion masiva", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SincronizacionMasivaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "415", description = "MediaType Unsopported", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<SincronizacionMasivaDTO>> sincronizacionMasiva(@RequestPart FilePart file,
                                                                              final ServerHttpRequest request) {
        return solicitarSincronizacionMasivaUseCase.solicitarSincronizacion(ArchivoMapper.crearArchivoDTO(file))
                .map( sincronizacionMasivaDTO ->
                        ResponseEntity
                                .created(
                                        URI.create(request.getURI().toString()
                                                .split("\\?")[0]
                                                .concat("/")
                                                .concat(sincronizacionMasivaDTO.idSincronizacion())))
                                .body(sincronizacionMasivaDTO)
                );
    }
}
