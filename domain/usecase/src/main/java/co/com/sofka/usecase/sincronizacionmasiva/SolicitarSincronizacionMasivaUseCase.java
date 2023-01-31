package co.com.sofka.usecase.sincronizacionmasiva;

import co.com.sofka.model.archivo.dto.ArchivoDTO;
import co.com.sofka.model.archivo.factoria.ArchivoFactory;
import co.com.sofka.model.archivo.gateways.AlmacenamientoArchivoRepository;
import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import co.com.sofka.model.sincronizacionmasiva.factoria.SincronizacionMasivaDTOFactory;
import co.com.sofka.model.sincronizacionmasiva.factoria.SincronizacionMasivaFactory;
import co.com.sofka.model.sincronizacionmasiva.gateways.SincronizacionMasivaGateway;
import co.com.sofka.model.sincronizacionmasiva.gateways.SincronizacionMasivaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class SolicitarSincronizacionMasivaUseCase {
    private final AlmacenamientoArchivoRepository almacenamientoArchivoRepository;
    private final SincronizacionMasivaRepository sincronizacionMasivaRepository;
    private final SincronizacionMasivaGateway sincronizacionMasivaGateway;

    public Mono<SincronizacionMasivaDTO> solicitarSincronizacion(ArchivoDTO archivoDTO) {

        final String idSincronizacion = UUID.randomUUID().toString();

        return Mono.just(ArchivoFactory.crearArchivo(archivoDTO, idSincronizacion))
                .flatMap(almacenamientoArchivoRepository::subir)
                .map(solicitudSincronizacionMasivaExitosa ->
                        SincronizacionMasivaFactory.crearSincronizacionMasiva(idSincronizacion))
                .flatMap(sincronizacionMasivaRepository::save)
                .map(SincronizacionMasivaDTOFactory::crearSincronizacionMasivaDTO)
                .flatMap(sincronizacionMasivaGateway::solicitarInicioProcesoSincronizacion);
    }
}
