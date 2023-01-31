package co.com.sofka.api.sincronizacionmasiva;

import co.com.sofka.api.sincronizacionmasiva.mapper.ArchivoMapper;
import co.com.sofka.model.sincronizacionmasiva.dto.SincronizacionMasivaDTO;
import co.com.sofka.usecase.sincronizacionmasiva.ConsultarSincronizacionMasivaUseCase;
import co.com.sofka.usecase.sincronizacionmasiva.SolicitarSincronizacionMasivaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(value = "/sincronizaciones-masivas", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SincronizacionMasivaController {

    private final SolicitarSincronizacionMasivaUseCase solicitarSincronizacionMasivaUseCase;
    private final ConsultarSincronizacionMasivaUseCase consultarSincronizacionMasivaUseCase;

    @GetMapping()
    public Mono<ResponseEntity<Flux<SincronizacionMasivaDTO>>> consultarTodas() {
        return Mono.just(ResponseEntity.ok()
                .body(consultarSincronizacionMasivaUseCase.consultarTodas()));
    }

    @GetMapping("/{idSincronizacion}")
    public Mono<ResponseEntity<SincronizacionMasivaDTO>> consultarSincronizacion(@PathVariable String idSincronizacion) {
        return consultarSincronizacionMasivaUseCase.consultarSincronizacion(idSincronizacion)
                .map(ResponseEntity::ok);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<SincronizacionMasivaDTO>> sincronizacionMasiva(@RequestPart FilePart file,
                                                                              final ServerHttpRequest request) {
        return solicitarSincronizacionMasivaUseCase.solicitarSincronizacion(ArchivoMapper.crearArchivoDTO(file))
                .map( sincronizacionMasivaDTO ->
                        ResponseEntity
                                .created(
                                        URI.create(request.getURI().toString()
                                                .split("\\?")[0]
                                                .concat("/")
                                                .concat(sincronizacionMasivaDTO.idSincronizacion())))
                                .body(sincronizacionMasivaDTO)
                );
    }
}
