package co.com.sofka.model.cliente.gateways;

import co.com.sofka.model.cliente.Cliente;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClienteRepository {
    Flux<Cliente> findAll();
    Mono<Cliente> findById(String nit);
    Mono<Cliente> save(Cliente cliente);
}
