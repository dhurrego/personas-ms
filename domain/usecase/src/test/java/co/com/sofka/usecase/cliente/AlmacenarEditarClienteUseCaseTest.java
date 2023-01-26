package co.com.sofka.usecase.cliente;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.cliente.dto.ClienteDTO;
import co.com.sofka.model.cliente.gateways.ClienteRepository;
import co.com.sofka.model.exception.negocio.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlmacenarEditarClienteUseCaseTest {

    private static final String NIT = "9055456556";
    private static final String RAZON_SOCIAL = "EMPRESA TEST";

    @InjectMocks
    private AlmacenarEditarClienteUseCase useCase;

    @Mock
    private ClienteRepository clienteRepository;

    private ClienteDTO clienteDTO;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        clienteDTO = ClienteDTO.builder()
                .nit(NIT)
                .razonSocial(RAZON_SOCIAL)
                .build();

        cliente = Cliente.builder()
                .nit(NIT)
                .razonSocial(RAZON_SOCIAL)
                .build();
    }

    @Test
    void registrarClienteExitosamente() {
        when(clienteRepository.findById(anyString())).thenReturn(Mono.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(Mono.just(cliente));

        spy(clienteRepository);

        StepVerifier.create(useCase.registrarCliente(clienteDTO))
                .assertNext(clienteDTOResult -> {
                    assertEquals(NIT, clienteDTOResult.nit());
                    assertEquals(RAZON_SOCIAL, clienteDTOResult.razonSocial());
                })
                .verifyComplete();

        verify(clienteRepository, times(1)).findById(anyString());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void registroClienteExistenteFallido() {
        when(clienteRepository.findById(anyString())).thenReturn(Mono.just(cliente));

        spy(clienteRepository);

        StepVerifier.create(useCase.registrarCliente(clienteDTO))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException&&
                        BusinessException.Tipo.ERROR_CLIENTE_YA_EXISTE
                                .getMessage()
                                .equals(throwable.getMessage())
                ).verify();

        verify(clienteRepository, times(1)).findById(anyString());
        verify(clienteRepository, times(0)).save(any(Cliente.class));
    }

    @Test
    void actualizarClienteExitosamente() {
        when(clienteRepository.findById(anyString())).thenReturn(Mono.just(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(Mono.just(cliente));

        spy(clienteRepository);

        StepVerifier.create(useCase.actualizarCliente(clienteDTO))
                .assertNext(clienteDTOResult -> {
                    assertEquals(NIT, clienteDTOResult.nit());
                    assertEquals(RAZON_SOCIAL, clienteDTOResult.razonSocial());
                })
                .verifyComplete();

        verify(clienteRepository, times(1)).findById(anyString());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void actualizacionClienteNoExisteFallido() {
        when(clienteRepository.findById(anyString())).thenReturn(Mono.empty());

        spy(clienteRepository);

        StepVerifier.create(useCase.actualizarCliente(clienteDTO))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        BusinessException.Tipo.ERROR_CLIENTE_NO_ENCONTRADO
                                    .getMessage()
                                    .equals(throwable.getMessage())
                ).verify();

        verify(clienteRepository, times(1)).findById(anyString());
        verify(clienteRepository, times(0)).save(any(Cliente.class));
    }
}