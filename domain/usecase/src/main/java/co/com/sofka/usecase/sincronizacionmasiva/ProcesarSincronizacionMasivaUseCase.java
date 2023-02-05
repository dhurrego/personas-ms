package co.com.sofka.usecase.sincronizacionmasiva;

import co.com.sofka.model.archivo.gateways.AlmacenamientoArchivoRepository;
import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.detallesincronizacion.factoria.DetalleSincronizacionFactory;
import co.com.sofka.model.detallesincronizacion.gateways.DetalleSincronizacionRepository;
import co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion;
import co.com.sofka.model.sincronizacionmasiva.SincronizacionMasiva;
import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import co.com.sofka.model.sincronizacionmasiva.gateways.ExcelGateway;
import co.com.sofka.model.sincronizacionmasiva.gateways.SincronizacionMasivaRepository;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.dto.AsignarClienteDTO;
import co.com.sofka.model.sofkiano.dto.SofkianoMasivoDTO;
import co.com.sofka.model.sofkiano.factoria.SofkianoFactory;
import co.com.sofka.model.sofkiano.gateways.SofkianoRepository;
import co.com.sofka.usecase.sofkiano.CambiarEstadoSofkianoUseCase;
import co.com.sofka.usecase.sofkiano.ModificarAsignacionUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_ESTADO_INVALIDO_SINCRONIZACION_MASIVA;
import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_PROCESO_SINCRONIZACION_MASIVA_NO_ENCONTRADO;

@RequiredArgsConstructor
public class ProcesarSincronizacionMasivaUseCase {

    private static final String FORMATO_DETALLE_ERROR = "La fila %s contiene el siguiente Error: %s";
    private final AlmacenamientoArchivoRepository almacenamientoArchivoRepository;
    private final SincronizacionMasivaRepository sincronizacionMasivaRepository;
    private final DetalleSincronizacionRepository detalleSincronizacionRepository;
    private final ExcelGateway excelGateway;
    private final SofkianoRepository sofkianoRepository;
    private final ModificarAsignacionUseCase modificarAsignacionUseCase;
    private final CambiarEstadoSofkianoUseCase cambiarEstadoSofkianoUseCase;

    public Mono<Boolean> sincronizar(SincronizacionMasivaDTO sincronizacionMasivaDTO) {
        final String idSincronizacion = sincronizacionMasivaDTO.idSincronizacion();
        final String extension = "xlsx";
        final String nombreArchivo = idSincronizacion.concat(".").concat(extension);

        return Mono.just(sincronizacionMasivaDTO.idSincronizacion())
                .flatMap(sincronizacionMasivaRepository::findById)
                .switchIfEmpty(Mono.error(ERROR_PROCESO_SINCRONIZACION_MASIVA_NO_ENCONTRADO::build))
                .flatMap(this::actualizarEstadoSincronizacion)
                .flatMap(sincronizacionMasiva -> almacenamientoArchivoRepository.descargar(nombreArchivo))
                .flatMapMany(excelGateway::convertirExcelASofkiano)
                .flatMap(sofkianoMasivoDTO -> this.actualizarInformacionSofkiano(sofkianoMasivoDTO, idSincronizacion))
                .collectList()
                .flatMap(sofkianos ->
                        sincronizacionMasivaRepository.finalizarSincronizacionMasiva(idSincronizacion, sofkianos.size()))
                .map(sincronizacionFinalizada -> Boolean.TRUE)
                .onErrorResume(throwable ->
                        sincronizacionMasivaRepository.rechazarSincronizacionMasiva(idSincronizacion)
                                .map(rechazado -> DetalleSincronizacionFactory.crearDetalle(idSincronizacion, throwable.getMessage()))
                                .flatMap(detalleSincronizacionRepository::save)
                                .flatMap(detalle -> Mono.error(throwable))
                );
    }

    private Mono<SincronizacionMasiva> actualizarEstadoSincronizacion(SincronizacionMasiva sincronizacionMasiva) {
        if (!sincronizacionMasiva.getEstado().equals(EstadoSincronizacion.CREADO)) {
            return Mono.error(ERROR_ESTADO_INVALIDO_SINCRONIZACION_MASIVA::build);
        }

        SincronizacionMasiva sincronizacionEnProceso = sincronizacionMasiva.toBuilder()
                .estado(EstadoSincronizacion.EN_PROCESO)
                .build();

        return sincronizacionMasivaRepository.save(sincronizacionEnProceso);
    }

    private Mono<Sofkiano> actualizarInformacionSofkiano(SofkianoMasivoDTO sofkianoMasivoDTO, String idSincronizacion) {
        return Mono.just(sofkianoMasivoDTO)
                .map(SofkianoFactory::crearSofkiano)
                .flatMap(this::construirSofkianoAGuardarActualizar)
                .flatMap(sofkianoRepository::save)
                .flatMap(sofkianoExistente -> activarOInactivarSofkiano(sofkianoMasivoDTO, sofkianoExistente))
                .flatMap(sofkianoExistente -> actualizarAsignacion(sofkianoMasivoDTO, sofkianoExistente))
                .flatMap(sofkianoExistente -> sofkianoRepository.findById(sofkianoExistente.getDni()))
                .onErrorResume(throwable -> sincronizacionMasivaRepository
                        .incrementarEjecucionesFallidas(idSincronizacion)
                        .map(actualizado ->
                                DetalleSincronizacionFactory.crearDetalle(idSincronizacion,
                                        String.format(FORMATO_DETALLE_ERROR, sofkianoMasivoDTO.numeroFila(),
                                                throwable.getMessage())))
                        .flatMap(detalleSincronizacionRepository::save)
                        .flatMap(detalle -> Mono.empty()));
    }

    private Mono<Sofkiano> activarOInactivarSofkiano(SofkianoMasivoDTO sofkianoMasivoDTO, Sofkiano sofkianoExistente) {
        if (sofkianoMasivoDTO.activo() != sofkianoExistente.isActivo()) {
            if (sofkianoMasivoDTO.activo()) {
                return cambiarEstadoSofkianoUseCase.activarSofkiano(sofkianoExistente.getDni())
                        .map(activo -> sofkianoExistente);
            }
            return cambiarEstadoSofkianoUseCase.inactivarSofkiano(sofkianoExistente.getDni())
                    .map(activo -> sofkianoExistente);
        }
        return Mono.just(sofkianoExistente);
    }

    private Mono<Sofkiano> actualizarAsignacion(SofkianoMasivoDTO sofkianoMasivoDTO, Sofkiano sofkianoExistente) {
        return Mono.justOrEmpty(sofkianoMasivoDTO.nitCliente())
                .flatMap(nitClienteNuevo ->
                        Mono.justOrEmpty(sofkianoExistente.getCliente()
                                        .map(Cliente::getNit)
                                        .filter(nitClienteNuevo::equals))
                                .switchIfEmpty(Mono.just(new AsignarClienteDTO(sofkianoExistente.getDni(), nitClienteNuevo))
                                        .flatMap(modificarAsignacionUseCase::asignarCliente)))
                .map(nit -> sofkianoExistente)
                .switchIfEmpty(Mono.justOrEmpty(sofkianoExistente.getCliente())
                        .flatMap(clienteExistente -> modificarAsignacionUseCase.retirarAsignacion(sofkianoExistente.getDni()))
                        .map(retirado -> sofkianoExistente)
                        .defaultIfEmpty(sofkianoExistente)
                );
    }

    private Mono<Sofkiano> construirSofkianoAGuardarActualizar(Sofkiano sofkiano) {
        return sofkianoRepository.findById(sofkiano.getDni())
                .map(sofkianoExistente -> sofkiano.toBuilder()
                        .fechaCreacion(sofkianoExistente.getFechaCreacion())
                        .activo(sofkianoExistente.isActivo())
                        .fechaSalida(sofkianoExistente.getFechaSalida())
                        .cliente(sofkianoExistente.getCliente())
                        .build())
                .defaultIfEmpty(sofkiano);
    }
}
