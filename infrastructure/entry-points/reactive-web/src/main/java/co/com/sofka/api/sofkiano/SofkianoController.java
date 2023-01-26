package co.com.sofka.api.sofkiano;

import co.com.sofka.api.dto.RespuestaGenericaDTO;
import co.com.sofka.model.sofkiano.dto.AsignarClienteDTO;
import co.com.sofka.model.sofkiano.dto.ConsolidadoAsignacionSofkianoDTO;
import co.com.sofka.model.sofkiano.dto.SofkianoARegistrarDTO;
import co.com.sofka.model.sofkiano.dto.SofkianoDTO;
import co.com.sofka.usecase.sofkiano.AlmacenarEditarSofkianoUseCase;
import co.com.sofka.usecase.sofkiano.CambiarEstadoSofkianoUseCase;
import co.com.sofka.usecase.sofkiano.ConsultarSofkianosUseCase;
import co.com.sofka.usecase.sofkiano.ModificarAsignacionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(value = "/sofkianos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SofkianoController {

    private final ConsultarSofkianosUseCase consultarSofkianosUseCase;
    private final AlmacenarEditarSofkianoUseCase almacenarEditarSofkianoUseCase;
    private final CambiarEstadoSofkianoUseCase cambiarEstadoSofkianoUseCase;
    private final ModificarAsignacionUseCase modificarAsignacionUseCase;

    @GetMapping
    public Mono<ResponseEntity<Flux<SofkianoDTO>>> listarTodos() {
        return Mono.just(ResponseEntity.ok()
                .body(consultarSofkianosUseCase.listarTodos()));
    }

    @GetMapping("/{dni}")
    public Mono<ResponseEntity<SofkianoDTO>> listarPorDni(@PathVariable String dni) {
        return consultarSofkianosUseCase.listarPorDni(dni).map(ResponseEntity::ok);
    }

    @GetMapping("/consolidado-asignacion")
    public Mono<ResponseEntity<ConsolidadoAsignacionSofkianoDTO>> obtenerConsolidadoAsignacion() {
        return consultarSofkianosUseCase.obtenerConsolidadoAsignacion().map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<SofkianoDTO>> registrarSofkiano(@RequestBody SofkianoARegistrarDTO sofkianoARegistrarDTO,
                                                               final ServerHttpRequest request) {
        return almacenarEditarSofkianoUseCase.registrarSofkiano(sofkianoARegistrarDTO)
                .map(sofkianoDTO ->
                        ResponseEntity
                                .created(
                                        URI.create(request.getURI()
                                                .toString()
                                                .concat("/")
                                                .concat(
                                                        sofkianoDTO.tipoIdentificacion()
                                                                .concat(sofkianoDTO.numeroIdentificacion()))))
                                .body(sofkianoDTO)
                );
    }

    @PutMapping
    public Mono<ResponseEntity<SofkianoDTO>> actualizarSofkiano(
            @RequestBody SofkianoARegistrarDTO sofkianoARegistrarDTO) {
        return almacenarEditarSofkianoUseCase.actualizarSofkiano(sofkianoARegistrarDTO).map(ResponseEntity::ok);
    }

    @PatchMapping("/activar/{dni}")
    public Mono<ResponseEntity<RespuestaGenericaDTO>> activarSofkiano(@PathVariable String dni) {
        return cambiarEstadoSofkianoUseCase.activarSofkiano(dni)
                .map(RespuestaGenericaDTO::new)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/asignar-cliente")
    public Mono<ResponseEntity<RespuestaGenericaDTO>> asignarCliente(@RequestBody AsignarClienteDTO asignarClienteDTO) {
        return modificarAsignacionUseCase.asignarCliente(asignarClienteDTO)
                .map(RespuestaGenericaDTO::new)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/retirar-asignacion/{dni}")
    public Mono<ResponseEntity<RespuestaGenericaDTO>> retirarAsignacion(@PathVariable String dni) {
        return modificarAsignacionUseCase.retirarAsignacion(dni)
                .map(RespuestaGenericaDTO::new)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/inactivar/{dni}")
    public Mono<ResponseEntity<RespuestaGenericaDTO>> inactivarSofkiano(@PathVariable String dni) {
        return cambiarEstadoSofkianoUseCase.inactivarSofkiano(dni)
                .map(RespuestaGenericaDTO::new)
                .map(ResponseEntity::ok);
    }
}
