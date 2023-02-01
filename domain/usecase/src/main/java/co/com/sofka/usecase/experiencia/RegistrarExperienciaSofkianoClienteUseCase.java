package co.com.sofka.usecase.experiencia;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.experiencia.ExperienciaSofkianoCliente;
import co.com.sofka.model.experiencia.dto.RegistrarExperienciaSofkianoDTO;
import co.com.sofka.model.experiencia.factoria.ExperienciaSofkianoClienteFactory;
import co.com.sofka.model.experiencia.gateways.ExperienciaSofkianoClienteRepository;
import co.com.sofka.model.sofkiano.dto.SofkianoDTO;
import co.com.sofka.usecase.sofkiano.ConsultarSofkianosUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_SOFKIANO_NO_ASIGNADO_AL_CLIENTE;

@RequiredArgsConstructor
public class RegistrarExperienciaSofkianoClienteUseCase {

    private final ConsultarSofkianosUseCase consultarSofkianosUseCase;
    private final ExperienciaSofkianoClienteRepository repository;

    public Mono<String> registrarExperienciaCliente(RegistrarExperienciaSofkianoDTO registrarExperienciaSofkianoDTO) {

        final String dniSofkiano = registrarExperienciaSofkianoDTO.dniSofkiano();
        final String nitCliente = registrarExperienciaSofkianoDTO.nitCliente();

        return Mono.just(registrarExperienciaSofkianoDTO)
                .map(ExperienciaSofkianoClienteFactory::crearExperienciaSofkianoCliente)
                .flatMap( experiencia -> validarSiSofkianoExiste(dniSofkiano, nitCliente, experiencia))
                .flatMap(repository::save)
                .map( experiencia -> "Se registro la experiencia del sofkiano con el cliente exitosamente");
    }

    private Mono<ExperienciaSofkianoCliente> validarSiSofkianoExiste(String dniSofkiano, String nitCliente, ExperienciaSofkianoCliente experiencia) {
        return consultarSofkianosUseCase.listarPorDni(dniSofkiano)
                .map(sofkianoDTO -> validarSiSofkianoPerteneceAlCliente(nitCliente, sofkianoDTO))
                .map(nit -> experiencia);
    }

    private static String validarSiSofkianoPerteneceAlCliente(String nitCliente, SofkianoDTO sofkianoDTO) {
        return sofkianoDTO.cliente()
                .map(Cliente::getNit)
                .filter(nitCliente::equals)
                .orElseThrow(ERROR_SOFKIANO_NO_ASIGNADO_AL_CLIENTE::build);
    }
}
