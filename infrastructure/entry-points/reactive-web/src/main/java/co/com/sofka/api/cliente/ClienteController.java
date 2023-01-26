package co.com.sofka.api.cliente;

import co.com.sofka.model.cliente.dto.ClienteDTO;
import co.com.sofka.usecase.cliente.AlmacenarEditarClienteUseCase;
import co.com.sofka.usecase.cliente.ConsultarClientesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @GetMapping
    public Mono<ResponseEntity<Flux<ClienteDTO>>> listarTodos() {
        return Mono.just(ResponseEntity.ok()
                .body(consultarClientesUseCase.listarTodos()));
    }

    @GetMapping("/{nit}")
    public Mono<ResponseEntity<ClienteDTO>> listarPorNit(@PathVariable String nit) {
        return consultarClientesUseCase.listarPorNit(nit).map(ResponseEntity::ok);
    }

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

    @PutMapping
    public Mono<ResponseEntity<ClienteDTO>> actualizarSofkiano(@RequestBody ClienteDTO clienteDTO) {
        return almacenarEditarClienteUseCase.actualizarCliente(clienteDTO).map(ResponseEntity::ok);
    }
}
