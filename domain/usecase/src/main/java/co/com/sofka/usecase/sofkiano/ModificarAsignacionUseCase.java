package co.com.sofka.usecase.sofkiano;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.estadisticas.dto.HistorialAsignacionSofkianoDTO;
import co.com.sofka.model.estadisticas.enums.TipoMovimiento;
import co.com.sofka.model.estadisticas.factoria.HistorialAsignacionSofkianoDTOFactory;
import co.com.sofka.model.estadisticas.gateways.CambioAsignacionGateway;
import co.com.sofka.model.sofkiano.AsignarClienteASofkiano;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.dto.AsignarClienteDTO;
import co.com.sofka.model.sofkiano.factoria.AsignarClienteASofkianoFactory;
import co.com.sofka.model.sofkiano.gateways.SofkianoRepository;
import co.com.sofka.usecase.cliente.ConsultarClientesUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.*;

@RequiredArgsConstructor
public class ModificarAsignacionUseCase {

    private static final String RETIRO_ASIGNACION_EXITOSO = "Se retiro la asignación del sofkiano";
    private static final String ASIGNACION_CORRECTAMENTE = "Se asignó el cliente correctamente";
    private final SofkianoRepository sofkianoRepository;
    private final ConsultarClientesUseCase consultarClientesUseCase;
    private final CambioAsignacionGateway cambioAsignacionGateway;

    public Mono<String> asignarCliente(AsignarClienteDTO asignarClienteDTO) {
        return Mono.just(AsignarClienteASofkianoFactory.crearAsignarClienteASofkiano(asignarClienteDTO))
                .map(AsignarClienteASofkiano::nitCliente)
                .flatMap(consultarClientesUseCase::listarPorNit)
                .flatMap(cliente -> sofkianoRepository.findById(asignarClienteDTO.dniSofkiano()))
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_NO_ENCONTRADO::build))
                .filter(Sofkiano::isActivo)
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_INACTIVO::build))
                .filter( sofkianoExistente -> validarSiClieneNuevoYaFueAsignado(asignarClienteDTO, sofkianoExistente))
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_ASIGNADO_MISMO_CLIENTE::build))
                .flatMap(sofkianoExistente -> {
                    Sofkiano sofkianoNuevo = sofkianoExistente.toBuilder()
                            .cliente(Optional.of(Cliente.builder()
                                    .nit(asignarClienteDTO.nitCliente())
                                    .build()))
                            .fechaActualizacion(LocalDateTime.now())
                            .build();

                    return actualizarSofkiano(sofkianoExistente, sofkianoNuevo);
                }).flatMap(sofkianoActualizado -> reportarHistorial(sofkianoActualizado, TipoMovimiento.INGRESO))
                .map(sofkianoActualizado -> ASIGNACION_CORRECTAMENTE);
    }

    public Mono<String> retirarAsignacion(String dni) {
        return Mono.just(dni)
                .flatMap(sofkianoRepository::findById)
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_NO_ENCONTRADO::build))
                .filter(Sofkiano::isActivo)
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_INACTIVO::build))
                .filter(sofkiano -> sofkiano.getCliente().isPresent())
                .switchIfEmpty(Mono.error(ERROR_SOFKIANO_SIN_CLIENTE_ASIGNADO::build))
                .flatMap(sofkianoExistente -> {
                    Sofkiano sofkianoNuevo = sofkianoExistente.toBuilder()
                            .cliente(Optional.empty())
                            .fechaActualizacion(LocalDateTime.now())
                            .build();

                    return actualizarSofkiano(sofkianoExistente, sofkianoNuevo);
                })
                .map(sofkianoActualizado -> RETIRO_ASIGNACION_EXITOSO)
                .defaultIfEmpty(RETIRO_ASIGNACION_EXITOSO);
    }

    private boolean validarSiClieneNuevoYaFueAsignado(AsignarClienteDTO asignarClienteDTO, Sofkiano sofkianoExistente) {
        return sofkianoExistente.getCliente()
                .map(Cliente::getNit)
                .filter(nitActual -> nitActual.equals(asignarClienteDTO.nitCliente()))
                .isEmpty();
    }

    private Mono<Sofkiano> actualizarSofkiano(Sofkiano sofkianoExistente, Sofkiano sofkianoNuevo) {
        return sofkianoRepository.save(sofkianoNuevo)
                .flatMap(sofkianoActualizado ->
                        reportarHistorial(sofkianoExistente, TipoMovimiento.SALIDA)
                                .map(sofkianoAnterior -> sofkianoActualizado));
    }

    private Mono<Sofkiano> reportarHistorial(Sofkiano sofkiano, TipoMovimiento tipoMovimiento) {
        return Mono.justOrEmpty(sofkiano.getCliente())
                .map( cliente -> construirHistorial(sofkiano, cliente, tipoMovimiento))
                .flatMap(cambioAsignacionGateway::reportarCambioAsignacionSofkiano)
                .map(reporteExitoso -> sofkiano)
                .defaultIfEmpty(sofkiano);
    }

    private HistorialAsignacionSofkianoDTO construirHistorial(Sofkiano sofkiano,
                                                              Cliente cliente,
                                                              TipoMovimiento tipoMovimiento) {
        return HistorialAsignacionSofkianoDTOFactory
                .crearAgregarHistorialAsignacionDTO(sofkiano.getDni(),
                        construirNombreCompletoSofkiano(sofkiano),
                        cliente.getNit(), cliente.getRazonSocial(), tipoMovimiento);
    }

    private String construirNombreCompletoSofkiano(Sofkiano sofkiano) {
        return sofkiano.getPrimerNombre().concat(" ")
                .concat(sofkiano.getSegundoNombre().orElse(""))
                .concat(" ")
                .concat(sofkiano.getPrimerApellido())
                .concat(" ")
                .concat(sofkiano.getSegundoApellido().orElse(""))
                .trim();
    }
}
