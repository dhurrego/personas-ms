package co.com.sofka.usecase.cliente;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.cliente.gateways.ClienteRepository;
import co.com.sofka.model.exception.negocio.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarClientesUseCaseTest {
    private static final String NIT = "9055456556";
    private static final String RAZON_SOCIAL = "EMPRESA TEST";

    @InjectMocks
    private ConsultarClientesUseCase useCase;

    @Mock
    private ClienteRepository clienteRepository;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .nit(NIT)
                .razonSocial(RAZON_SOCIAL)
                .build();
    }

    @Test
    void listarTodos() {
        when(clienteRepository.findAll()).thenReturn(Flux.fromIterable(Collections.singletonList(cliente)));

        spy(clienteRepository);

        StepVerifier.create(useCase.listarTodos())
                .assertNext(clienteDTO -> {
                    assertEquals(NIT, clienteDTO.nit());
                    assertEquals(RAZON_SOCIAL, clienteDTO.razonSocial());
                })
                .verifyComplete();

        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void listarPorNitClienteExistente() {
        when(clienteRepository.findById(anyString())).thenReturn(Mono.just(cliente));

        spy(clienteRepository);

        StepVerifier.create(useCase.listarPorNit(NIT))
                .assertNext(clienteDTO -> {
                    assertEquals(NIT, clienteDTO.nit());
                    assertEquals(RAZON_SOCIAL, clienteDTO.razonSocial());
                })
                .verifyComplete();

        verify(clienteRepository, times(1)).findById(anyString());
    }

    @Test
    void listarPorNitClienteNoExiste() {
        when(clienteRepository.findById(anyString())).thenReturn(Mono.empty());

        spy(clienteRepository);

        StepVerifier.create(useCase.listarPorNit(NIT))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(useCase.listarPorNit(NIT))
                .expectErrorMessage(BusinessException.Tipo.ERROR_CLIENTE_NO_ENCONTRADO.getMessage())
                .verify();

        verify(clienteRepository, times(2)).findById(anyString());
    }
}