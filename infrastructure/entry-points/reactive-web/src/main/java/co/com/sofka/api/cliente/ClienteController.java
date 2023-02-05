package co.com.sofka.api.cliente;

import co.com.sofka.model.cliente.dto.ClienteDTO;
import co.com.sofka.usecase.cliente.AlmacenarEditarClienteUseCase;
import co.com.sofka.usecase.cliente.ConsultarClientesUseCase;
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
@RequestMapping(value = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ClienteController {

    private final ConsultarClientesUseCase consultarClientesUseCase;
    private final AlmacenarEditarClienteUseCase almacenarEditarClienteUseCase;

    @Operation(summary = "Servicio para consultar la información de todos los clientes. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @GetMapping
    public Mono<ResponseEntity<Flux<ClienteDTO>>> listarTodos() {
        return Mono.just(ResponseEntity.ok()
                .body(consultarClientesUseCase.listarTodos()));
    }

    @Operation(summary = "Servicio para consultar la información de un cliente a través del nit. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @GetMapping("/{nit}")
    public Mono<ResponseEntity<ClienteDTO>> listarPorNit(@PathVariable String nit) {
        return consultarClientesUseCase.listarPorNit(nit).map(ResponseEntity::ok);
    }

    @Operation(summary = "Servicio para guardar un cliente nuevo. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @PostMapping
    public Mono<ResponseEntity<ClienteDTO>> registrarCliente(@RequestBody ClienteDTO clienteDTO,
                                                             final ServerHttpRequest request) {
        return almacenarEditarClienteUseCase.registrarCliente(clienteDTO)
                .map(cliente ->
                        ResponseEntity
                                .created(
                                        URI.create(request.getURI()
                                                .toString()
                                                .concat("/")
                                                .concat(cliente.nit())))
                                .body(cliente)
                );
    }

    @Operation(summary = "Servicio para actualizar la información de un cliente. ", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))) })
    @PutMapping
    public Mono<ResponseEntity<ClienteDTO>> actualizarSofkiano(@RequestBody ClienteDTO clienteDTO) {
        return almacenarEditarClienteUseCase.actualizarCliente(clienteDTO).map(ResponseEntity::ok);
    }
}
