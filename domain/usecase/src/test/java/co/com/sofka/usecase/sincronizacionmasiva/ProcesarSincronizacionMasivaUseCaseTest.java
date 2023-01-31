package co.com.sofka.usecase.sincronizacionmasiva;

import co.com.sofka.model.archivo.gateways.AlmacenamientoArchivoRepository;
import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.detallesincronizacion.DetalleSincronizacion;
import co.com.sofka.model.detallesincronizacion.gateways.DetalleSincronizacionRepository;
import co.com.sofka.model.exception.negocio.BusinessException;
import co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion;
import co.com.sofka.model.sincronizacionmasiva.SincronizacionMasiva;
import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import co.com.sofka.model.sincronizacionmasiva.gateways.ExcelGateway;
import co.com.sofka.model.sincronizacionmasiva.gateways.SincronizacionMasivaRepository;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.dto.AsignarClienteDTO;
import co.com.sofka.model.sofkiano.dto.SofkianoMasivoDTO;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import co.com.sofka.model.sofkiano.gateways.SofkianoRepository;
import co.com.sofka.usecase.sofkiano.CambiarEstadoSofkianoUseCase;
import co.com.sofka.usecase.sofkiano.ModificarAsignacionUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.*;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_ESTADO_INVALIDO_SINCRONIZACION_MASIVA;
import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_PROCESO_SINCRONIZACION_MASIVA_NO_ENCONTRADO;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcesarSincronizacionMasivaUseCaseTest {

    private static final String ID_SINCRONIZACION = UUID.randomUUID().toString();
    private static final String TIPO_IDENTIFICACION = "CC";
    private static final String NUMERO_IDENTIFICACION = "123123123";
    private static final String STRING_TEST = "TEST";

    @InjectMocks
    private ProcesarSincronizacionMasivaUseCase procesarSincronizacionMasivaUseCase;

    @Mock
    private AlmacenamientoArchivoRepository almacenamientoArchivoRepository;

    @Mock
    private SincronizacionMasivaRepository sincronizacionMasivaRepository;

    @Mock
    private DetalleSincronizacionRepository detalleSincronizacionRepository;

    @Mock
    private ExcelGateway excelGateway;

    @Mock
    private SofkianoRepository sofkianoRepository;

    @Mock
    private ModificarAsignacionUseCase modificarAsignacionUseCase;

    @Mock
    private CambiarEstadoSofkianoUseCase cambiarEstadoSofkianoUseCase;

    private SincronizacionMasivaDTO sincronizacionMasivaDTO;
    private SincronizacionMasiva sincronizacionMasiva;
    private SofkianoMasivoDTO sofkianoMasivoDTO;
    private Sofkiano sofkiano;
    private DetalleSincronizacion detalleSincronizacion;

    @BeforeEach
    void setUp() {
        sincronizacionMasivaDTO = SincronizacionMasivaDTO.builder()
                .idSincronizacion(ID_SINCRONIZACION)
                .estado(EstadoSincronizacion.CREADO)
                .ejecucionesExitosas(0)
                .ejecucionesFallidas(0)
                .detallesSincronizacion(Collections.emptyList())
                .build();

        sincronizacionMasiva = SincronizacionMasiva.builder()
                .idSincronizacion(ID_SINCRONIZACION)
                .estado(EstadoSincronizacion.CREADO)
                .ejecucionesExitosas(0)
                .ejecucionesFallidas(0)
                .detallesSincronizacion(Collections.emptyList())
                .build();

        sofkianoMasivoDTO = SofkianoMasivoDTO.builder()
                .numeroFila(1)
                .tipoIdentificacion(TIPO_IDENTIFICACION)
                .numeroIdentificacion(NUMERO_IDENTIFICACION)
                .primerNombre(STRING_TEST)
                .segundoNombre(Optional.empty())
                .primerApellido(STRING_TEST)
                .segundoApellido(Optional.empty())
                .direccion(STRING_TEST)
                .activo(true)
                .nitCliente(Optional.empty())
                .build();

        sofkiano = Sofkiano.builder()
                .dni(TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION))
                .tipoIdentificacion(TipoIdentificacion.valueOf(TIPO_IDENTIFICACION))
                .numeroIdentificacion(NUMERO_IDENTIFICACION)
                .primerNombre(STRING_TEST)
                .segundoNombre(Optional.of(STRING_TEST))
                .primerApellido(STRING_TEST)
                .segundoApellido(Optional.of(STRING_TEST))
                .activo(true)
                .direccion(STRING_TEST)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .fechaSalida(Optional.empty())
                .cliente(Optional.empty())
                .build();

        detalleSincronizacion = DetalleSincronizacion.builder()
                .idSincronizacion(ID_SINCRONIZACION)
                .descripcionFallido(STRING_TEST)
                .idDetalle(1)
                .build();

        when(sincronizacionMasivaRepository.findById(anyString()))
                .thenReturn(Mono.just(sincronizacionMasiva));
    }

    @Test
    void sincronizarTodosRegistrosExitosos() {
        when(sincronizacionMasivaRepository.finalizarSincronizacionMasiva(anyString(), anyInt()))
                .thenReturn(Mono.just(Boolean.TRUE));

        when(sincronizacionMasivaRepository.save(any(SincronizacionMasiva.class)))
                .thenReturn(Mono.just(sincronizacionMasiva.toBuilder()
                        .estado(EstadoSincronizacion.EN_PROCESO).build()));

        when(almacenamientoArchivoRepository.descargar(any()))
                .thenReturn(Mono.just(new byte[0]));

        when(excelGateway.convertirExcelASofkiano(any()))
                .thenReturn(Flux.just(sofkianoMasivoDTO));

        when(sofkianoRepository.findById(anyString()))
                .thenReturn(Mono.just(sofkiano));

        when(sofkianoRepository.save(any(Sofkiano.class)))
                .thenReturn(Mono.just(sofkiano));

        StepVerifier.create(procesarSincronizacionMasivaUseCase.sincronizar(sincronizacionMasivaDTO))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void sincronizarConAlgunosRegistrosFallidos() {

        SofkianoMasivoDTO sofkianoMasivoDTOConFallas = SofkianoMasivoDTO.builder()
                .numeroFila(2)
                .tipoIdentificacion("TT")
                .numeroIdentificacion(NUMERO_IDENTIFICACION)
                .primerNombre(STRING_TEST)
                .segundoNombre(Optional.empty())
                .primerApellido(STRING_TEST)
                .segundoApellido(Optional.empty())
                .direccion(STRING_TEST)
                .activo(true)
                .nitCliente(Optional.empty())
                .build();

        List<SofkianoMasivoDTO> sofkianosMasivosDTO = Arrays.asList(sofkianoMasivoDTO, sofkianoMasivoDTOConFallas);

        when(excelGateway.convertirExcelASofkiano(any()))
                .thenReturn(Flux.fromIterable(sofkianosMasivosDTO));

        when(detalleSincronizacionRepository.save(any(DetalleSincronizacion.class)))
                .thenReturn(Mono.just(detalleSincronizacion));

        when(sincronizacionMasivaRepository.incrementarEjecucionesFallidas(anyString()))
                .thenReturn(Mono.just(Boolean.TRUE));

        when(sincronizacionMasivaRepository.finalizarSincronizacionMasiva(anyString(), anyInt()))
                .thenReturn(Mono.just(Boolean.TRUE));

        when(sincronizacionMasivaRepository.save(any(SincronizacionMasiva.class)))
                .thenReturn(Mono.just(sincronizacionMasiva.toBuilder()
                        .estado(EstadoSincronizacion.EN_PROCESO).build()));

        when(almacenamientoArchivoRepository.descargar(any()))
                .thenReturn(Mono.just(new byte[0]));

        when(sofkianoRepository.findById(anyString()))
                .thenReturn(Mono.just(sofkiano));

        when(sofkianoRepository.save(any(Sofkiano.class)))
                .thenReturn(Mono.just(sofkiano));

        StepVerifier.create(procesarSincronizacionMasivaUseCase.sincronizar(sincronizacionMasivaDTO))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();

        verify(detalleSincronizacionRepository, times(1)).save(any());
        verify(sincronizacionMasivaRepository, times(1)).incrementarEjecucionesFallidas(any());
    }

    @Test
    void sincronizacionFallidaPorProcesoNoEncontrado() {
        when(sincronizacionMasivaRepository.findById(anyString())).thenReturn(Mono.empty());

        when(detalleSincronizacionRepository.save(any(DetalleSincronizacion.class)))
                .thenReturn(Mono.just(detalleSincronizacion));

        when(sincronizacionMasivaRepository.rechazarSincronizacionMasiva(anyString()))
                .thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(procesarSincronizacionMasivaUseCase.sincronizar(sincronizacionMasivaDTO))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(procesarSincronizacionMasivaUseCase.sincronizar(sincronizacionMasivaDTO))
                .expectErrorMessage(ERROR_PROCESO_SINCRONIZACION_MASIVA_NO_ENCONTRADO.getMessage())
                .verify();

        verify(detalleSincronizacionRepository, times(2)).save(any());
        verify(sincronizacionMasivaRepository, times(2)).findById(anyString());
        verify(sincronizacionMasivaRepository, times(2)).rechazarSincronizacionMasiva(anyString());
    }

    @Test
    void sincronizacionFallidaPorProcesoConEstadoInvalido() {
        sincronizacionMasiva = sincronizacionMasiva.toBuilder()
                .estado(EstadoSincronizacion.EN_PROCESO)
                .build();

        when(sincronizacionMasivaRepository.findById(anyString())).thenReturn(Mono.just(sincronizacionMasiva));

        when(detalleSincronizacionRepository.save(any(DetalleSincronizacion.class)))
                .thenReturn(Mono.just(detalleSincronizacion));

        when(sincronizacionMasivaRepository.rechazarSincronizacionMasiva(anyString()))
                .thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(procesarSincronizacionMasivaUseCase.sincronizar(sincronizacionMasivaDTO))
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(procesarSincronizacionMasivaUseCase.sincronizar(sincronizacionMasivaDTO))
                .expectErrorMessage(ERROR_ESTADO_INVALIDO_SINCRONIZACION_MASIVA.getMessage())
                .verify();

        verify(detalleSincronizacionRepository, times(2)).save(any());
        verify(sincronizacionMasivaRepository, times(2)).findById(anyString());
        verify(sincronizacionMasivaRepository, times(2)).rechazarSincronizacionMasiva(anyString());
    }

    @Test
    void sincronizarTodosRegistrosExitososConAsignacionCliente() {
        when(sincronizacionMasivaRepository.finalizarSincronizacionMasiva(anyString(), anyInt()))
                .thenReturn(Mono.just(Boolean.TRUE));

        when(sincronizacionMasivaRepository.save(any(SincronizacionMasiva.class)))
                .thenReturn(Mono.just(sincronizacionMasiva.toBuilder()
                        .estado(EstadoSincronizacion.EN_PROCESO).build()));

        when(almacenamientoArchivoRepository.descargar(any()))
                .thenReturn(Mono.just(new byte[0]));

        sofkianoMasivoDTO = SofkianoMasivoDTO.builder()
                .numeroFila(1)
                .tipoIdentificacion(TIPO_IDENTIFICACION)
                .numeroIdentificacion(NUMERO_IDENTIFICACION)
                .primerNombre(STRING_TEST)
                .segundoNombre(Optional.empty())
                .primerApellido(STRING_TEST)
                .segundoApellido(Optional.empty())
                .direccion(STRING_TEST)
                .activo(true)
                .nitCliente(Optional.of(STRING_TEST))
                .build();

        when(excelGateway.convertirExcelASofkiano(any()))
                .thenReturn(Flux.just(sofkianoMasivoDTO));

        when(sofkianoRepository.findById(anyString()))
                .thenReturn(Mono.just(sofkiano));

        when(sofkianoRepository.save(any(Sofkiano.class)))
                .thenReturn(Mono.just(sofkiano));

        when(modificarAsignacionUseCase.asignarCliente(any(AsignarClienteDTO.class)))
                .thenReturn(Mono.just(STRING_TEST));

        StepVerifier.create(procesarSincronizacionMasivaUseCase.sincronizar(sincronizacionMasivaDTO))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();

        verify(modificarAsignacionUseCase, times(1)).asignarCliente(any());
    }

    @Test
    void sincronizarTodosRegistrosExitososRetirandoAsignacion() {
        when(sincronizacionMasivaRepository.finalizarSincronizacionMasiva(anyString(), anyInt()))
                .thenReturn(Mono.just(Boolean.TRUE));

        when(sincronizacionMasivaRepository.save(any(SincronizacionMasiva.class)))
                .thenReturn(Mono.just(sincronizacionMasiva.toBuilder()
                        .estado(EstadoSincronizacion.EN_PROCESO).build()));

        when(almacenamientoArchivoRepository.descargar(any()))
                .thenReturn(Mono.just(new byte[0]));

        when(excelGateway.convertirExcelASofkiano(any()))
                .thenReturn(Flux.just(sofkianoMasivoDTO));

        sofkiano = sofkiano.toBuilder()
                .cliente(Optional.of(Cliente.builder().nit("8906521452").build()))
                .build();

        when(sofkianoRepository.findById(anyString()))
                .thenReturn(Mono.just(sofkiano));


        when(sofkianoRepository.save(any(Sofkiano.class)))
                .thenReturn(Mono.just(sofkiano));

        when(modificarAsignacionUseCase.retirarAsignacion(anyString()))
                .thenReturn(Mono.just(STRING_TEST));

        StepVerifier.create(procesarSincronizacionMasivaUseCase.sincronizar(sincronizacionMasivaDTO))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();

        verify(modificarAsignacionUseCase, times(1)).retirarAsignacion(anyString());
    }

    @Test
    void sincronizarTodosRegistrosExitososInactivandoSofkiano() {
        when(sincronizacionMasivaRepository.finalizarSincronizacionMasiva(anyString(), anyInt()))
                .thenReturn(Mono.just(Boolean.TRUE));

        when(sincronizacionMasivaRepository.save(any(SincronizacionMasiva.class)))
                .thenReturn(Mono.just(sincronizacionMasiva.toBuilder()
                        .estado(EstadoSincronizacion.EN_PROCESO).build()));

        when(almacenamientoArchivoRepository.descargar(any()))
                .thenReturn(Mono.just(new byte[0]));

        sofkianoMasivoDTO = SofkianoMasivoDTO.builder()
                .numeroFila(1)
                .tipoIdentificacion(TIPO_IDENTIFICACION)
                .numeroIdentificacion(NUMERO_IDENTIFICACION)
                .primerNombre(STRING_TEST)
                .segundoNombre(Optional.empty())
                .primerApellido(STRING_TEST)
                .segundoApellido(Optional.empty())
                .direccion(STRING_TEST)
                .activo(false)
                .nitCliente(Optional.empty())
                .build();

        when(excelGateway.convertirExcelASofkiano(any()))
                .thenReturn(Flux.just(sofkianoMasivoDTO));

        when(sofkianoRepository.findById(anyString()))
                .thenReturn(Mono.just(sofkiano));


        when(sofkianoRepository.save(any(Sofkiano.class)))
                .thenReturn(Mono.just(sofkiano));

        when(cambiarEstadoSofkianoUseCase.inactivarSofkiano(anyString()))
                .thenReturn(Mono.just(STRING_TEST));

        StepVerifier.create(procesarSincronizacionMasivaUseCase.sincronizar(sincronizacionMasivaDTO))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();

        verify(cambiarEstadoSofkianoUseCase, times(1)).inactivarSofkiano(anyString());
    }

    @Test
    void sincronizarTodosRegistrosExitososActivandoSofkiano() {
        when(sincronizacionMasivaRepository.finalizarSincronizacionMasiva(anyString(), anyInt()))
                .thenReturn(Mono.just(Boolean.TRUE));

        when(sincronizacionMasivaRepository.save(any(SincronizacionMasiva.class)))
                .thenReturn(Mono.just(sincronizacionMasiva.toBuilder()
                        .estado(EstadoSincronizacion.EN_PROCESO).build()));

        when(almacenamientoArchivoRepository.descargar(any()))
                .thenReturn(Mono.just(new byte[0]));

        when(excelGateway.convertirExcelASofkiano(any()))
                .thenReturn(Flux.just(sofkianoMasivoDTO));

        sofkiano = sofkiano.toBuilder().activo(false).build();

        when(sofkianoRepository.findById(anyString()))
                .thenReturn(Mono.just(sofkiano));

        when(sofkianoRepository.save(any(Sofkiano.class)))
                .thenReturn(Mono.just(sofkiano));

        when(cambiarEstadoSofkianoUseCase.activarSofkiano(anyString()))
                .thenReturn(Mono.just(STRING_TEST));

        StepVerifier.create(procesarSincronizacionMasivaUseCase.sincronizar(sincronizacionMasivaDTO))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();

        verify(cambiarEstadoSofkianoUseCase, times(1)).activarSofkiano(anyString());
    }
}