package co.com.sofka.usecase.cliente;

import co.com.sofka.model.cliente.dto.ClienteDTO;
import co.com.sofka.model.cliente.factoria.ClienteDTOFactory;
import co.com.sofka.model.cliente.gateways.ClienteRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_CLIENTE_NO_ENCONTRADO;

@RequiredArgsConstructor
public class ConsultarClientesUseCase {

    private final ClienteRepository clienteRepository;

    public Flux<ClienteDTO> listarTodos() {
        return clienteRepository.findAll()
                .map(ClienteDTOFactory::crearCliente);
    }

    public Mono<ClienteDTO> listarPorNit(String nit) {
        return clienteRepository.findById(nit)
                .map(ClienteDTOFactory::crearCliente)
                .switchIfEmpty(Mono.error(ERROR_CLIENTE_NO_ENCONTRADO::build));
    }
}
