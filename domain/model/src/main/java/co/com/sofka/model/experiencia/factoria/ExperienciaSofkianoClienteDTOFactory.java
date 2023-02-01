package co.com.sofka.model.experiencia.factoria;

import co.com.sofka.model.experiencia.ExperienciaSofkianoCliente;
import co.com.sofka.model.experiencia.dto.ExperienciaSofkianoClienteDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExperienciaSofkianoClienteDTOFactory {

    public static ExperienciaSofkianoClienteDTO crearExperienciaSofkianoClienteDTO(
            ExperienciaSofkianoCliente experienciaSofkianoCliente) {
        return ExperienciaSofkianoClienteDTO.builder()
                .nitCliente(experienciaSofkianoCliente.getCliente().getNit())
                .dniSofkiano(experienciaSofkianoCliente.getSofkiano().getDni())
                .descripcion(experienciaSofkianoCliente.getDescripcion())
                .nivelSatisfaccion(experienciaSofkianoCliente.getNivelSatisfaccion())
                .fechaCreacion(experienciaSofkianoCliente.getFechaCreacion())
                .build();
    }
}
