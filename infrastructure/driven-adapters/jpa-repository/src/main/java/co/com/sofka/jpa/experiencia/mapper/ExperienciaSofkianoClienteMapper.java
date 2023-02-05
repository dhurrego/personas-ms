package co.com.sofka.jpa.experiencia.mapper;

import co.com.sofka.jpa.cliente.ClienteData;
import co.com.sofka.jpa.experiencia.data.ExperienciaSofkianoClienteData;
import co.com.sofka.jpa.sofkiano.data.SofkianoData;
import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.experiencia.ExperienciaSofkianoCliente;
import co.com.sofka.model.sofkiano.Sofkiano;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExperienciaSofkianoClienteMapper {

    public static ExperienciaSofkianoClienteData toData(ExperienciaSofkianoCliente entity) {

        ClienteData clienteData = ClienteData.builder()
                .nit(entity.getCliente().getNit())
                .build();

        SofkianoData sofkianoData = SofkianoData.builder()
                .dni(entity.getSofkiano().getDni())
                .build();

        return ExperienciaSofkianoClienteData.builder()
                .idExperiencia(entity.getIdExperiencia())
                .fechaCreacion(entity.getFechaCreacion())
                .nivelSatisfaccion(entity.getNivelSatisfaccion())
                .clienteData(clienteData)
                .sofkianoData(sofkianoData)
                .descripcion(entity.getDescripcion())
                .build();
    }

    public static ExperienciaSofkianoCliente toEntity(ExperienciaSofkianoClienteData data) {
        Cliente clienteData = Cliente.builder()
                .nit(data.getClienteData().getNit())
                .build();

        Sofkiano sofkianoData = Sofkiano.builder()
                .dni(data.getSofkianoData().getDni())
                .build();

        return ExperienciaSofkianoCliente.builder()
                .idExperiencia(data.getIdExperiencia())
                .fechaCreacion(data.getFechaCreacion())
                .nivelSatisfaccion(data.getNivelSatisfaccion())
                .cliente(clienteData)
                .sofkiano(sofkianoData)
                .descripcion(data.getDescripcion())
                .build();
    }
}
