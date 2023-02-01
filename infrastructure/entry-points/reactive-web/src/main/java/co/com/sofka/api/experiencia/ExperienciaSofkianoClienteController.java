package co.com.sofka.api.experiencia;

import co.com.sofka.api.dto.RespuestaGenericaDTO;
import co.com.sofka.model.experiencia.dto.ExperienciaSofkianoClienteDTO;
import co.com.sofka.model.experiencia.dto.PromedioCalificacionClienteDTO;
import co.com.sofka.model.experiencia.dto.RegistrarExperienciaSofkianoDTO;
import co.com.sofka.usecase.experiencia.ConsultarExperenciasSofkianosUseCase;
import co.com.sofka.usecase.experiencia.RegistrarExperienciaSofkianoClienteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/experiencias-sofkianos-cliente", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ExperienciaSofkianoClienteController {

    private final RegistrarExperienciaSofkianoClienteUseCase registrarExperienciaSofkianoClienteUseCase;
    private final ConsultarExperenciasSofkianosUseCase consultarExperenciasSofkianosUseCase;

    @GetMapping
    public Mono<ResponseEntity<Flux<ExperienciaSofkianoClienteDTO>>> consultarExperiencias() {
        return Mono.just(ResponseEntity.ok()
                .body(consultarExperenciasSofkianosUseCase.consultarTodasLasExperiencias()));
    }

    @GetMapping("/por-sofkiano/{dniSofkiano}")
    public Mono<ResponseEntity<Flux<ExperienciaSofkianoClienteDTO>>> consultarExperienciasPorSofkiano(
            @PathVariable String dniSofkiano
    ) {
        return Mono.just(ResponseEntity.ok()
                .body(consultarExperenciasSofkianosUseCase.consultarExperienciasPorSofkiano(dniSofkiano)));
    }

    @GetMapping("/por-sofkiano/{dniSofkiano}/{nitCliente}")
    public Mono<ResponseEntity<Flux<ExperienciaSofkianoClienteDTO>>> consultarExperienciasPorSofkianoYCliente(
            @PathVariable String dniSofkiano, @PathVariable String nitCliente
    ) {
        return Mono.just(ResponseEntity.ok()
                .body(consultarExperenciasSofkianosUseCase
                        .consultarExperienciasPorSofkianoYCliente(dniSofkiano, nitCliente)));
    }

    @GetMapping("/por-cliente/{nitCliente}")
    public Mono<ResponseEntity<Flux<ExperienciaSofkianoClienteDTO>>> consultarExperienciasPorCliente(
            @PathVariable String nitCliente
    ) {
        return Mono.just(ResponseEntity.ok()
                .body(consultarExperenciasSofkianosUseCase
                        .consultarExperienciasPorCliente(nitCliente)));
    }

    @GetMapping("/promedio-calificaciones-por-cliente")
    public Mono<ResponseEntity<PromedioCalificacionClienteDTO>> consultarPromedioExperienciasPorCliente(
            @RequestParam String nitCliente,
            @RequestParam LocalDate fechaInicial,
            @RequestParam LocalDate fechaFinal
    ) {
        return consultarExperenciasSofkianosUseCase
                .consultarPromedioCalificacionCliente(nitCliente, fechaInicial, fechaFinal)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/promedio-calificaciones-clientes")
    public Mono<ResponseEntity<Flux<PromedioCalificacionClienteDTO>>> consultarPromedioExperiencias(
            @RequestParam LocalDate fechaInicial,
            @RequestParam LocalDate fechaFinal
    ) {
        return Mono.just(ResponseEntity.ok()
                .body(consultarExperenciasSofkianosUseCase
                .consultarPromedioCalificaciones(fechaInicial, fechaFinal))
        );
    }

    @PostMapping
    public Mono<ResponseEntity<RespuestaGenericaDTO>> registrarExperiencia(
            @RequestBody RegistrarExperienciaSofkianoDTO registrarExperienciaSofkianoDTO,
            final ServerHttpRequest request) {
        return registrarExperienciaSofkianoClienteUseCase.registrarExperienciaCliente(registrarExperienciaSofkianoDTO)
                .map(RespuestaGenericaDTO::new)
                .map(respuesta ->
                        ResponseEntity
                                .created(
                                        URI.create(request.getURI()
                                                .toString()
                                                .concat("/por-sofkiano/")
                                                .concat(registrarExperienciaSofkianoDTO.dniSofkiano())
                                                .concat("/")
                                                .concat(registrarExperienciaSofkianoDTO.nitCliente())))
                                .body(respuesta));
    }
}
