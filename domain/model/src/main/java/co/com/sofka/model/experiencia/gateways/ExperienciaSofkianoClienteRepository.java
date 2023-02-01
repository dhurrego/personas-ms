package co.com.sofka.model.experiencia.gateways;

import co.com.sofka.model.experiencia.ExperienciaSofkianoCliente;
import co.com.sofka.model.experiencia.PromedioCalificacionCliente;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface ExperienciaSofkianoClienteRepository {
    Flux<ExperienciaSofkianoCliente> findAll();
    Mono<ExperienciaSofkianoCliente> save(ExperienciaSofkianoCliente experienciaSofkianoCliente);
    Flux<ExperienciaSofkianoCliente> consultarExperienciasPorSofkiano(String dniSofkiano);
    Flux<ExperienciaSofkianoCliente> consultarExperienciasPorSofkianoYCliente(String dniSofkiano, String nitCliente);
    Flux<ExperienciaSofkianoCliente> consultarExperienciasPorCliente(String nitCliente);
    Mono<PromedioCalificacionCliente> calcularPromedioCliente(String nitCliente, LocalDate fechaInicial, LocalDate fechaFinal);
    Flux<PromedioCalificacionCliente> calcularPromedioCalificaciones(LocalDate fechaInicial, LocalDate fechaFinal);

}
