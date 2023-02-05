package co.com.sofka.usecase.cliente;

import co.com.sofka.model.cliente.dto.ClienteDTO;
import co.com.sofka.model.cliente.factoria.ClienteDTOFactory;
import co.com.sofka.model.cliente.factoria.ClienteFactory;
import co.com.sofka.model.cliente.gateways.ClienteRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_CLIENTE_YA_EXISTE;
import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_CLIENTE_NO_ENCONTRADO;

@RequiredArgsConstructor
public class AlmacenarEditarClienteUseCase {
    private final ClienteRepository clienteRepository;

    public Mono<ClienteDTO> registrarCliente(ClienteDTO clienteDTO) {
        return Mono.just(clienteDTO.nit())
                .flatMap(clienteRepository::findById)
                .doOnNext( clienteExistente -> {
                    throw ERROR_CLIENTE_YA_EXISTE.build();
                })
                .switchIfEmpty(Mono.just(ClienteFactory.crearCliente(clienteDTO)))
                .flatMap(clienteRepository::save)
                .map(ClienteDTOFactory::crearCliente);
    }

    public Mono<ClienteDTO> actualizarCliente(ClienteDTO clienteDTO) {
        return Mono.just(clienteDTO.nit())
                .flatMap(clienteRepository::findById)
                .switchIfEmpty(Mono.error(ERROR_CLIENTE_NO_ENCONTRADO::build))
                .map(cliente -> ClienteFactory.crearCliente(clienteDTO))
                .flatMap(clienteRepository::save)
                .map(ClienteDTOFactory::crearCliente);
    }
}
