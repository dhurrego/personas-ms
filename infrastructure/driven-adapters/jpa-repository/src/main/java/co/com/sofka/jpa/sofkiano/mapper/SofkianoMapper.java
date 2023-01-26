package co.com.sofka.jpa.sofkiano.mapper;

import co.com.sofka.jpa.cliente.mapper.ClienteMapper;
import co.com.sofka.jpa.sofkiano.data.SofkianoData;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SofkianoMapper {

    public static Sofkiano toEntity(SofkianoData sofkianoData) {
        return Sofkiano.builder()
                .dni(sofkianoData.getDni())
                .tipoIdentificacion(TipoIdentificacion.valueOf(sofkianoData.getTipoIdentificacion()))
                .numeroIdentificacion(sofkianoData.getNumeroIdentificacion())
                .primerNombre(sofkianoData.getPrimerNombre())
                .segundoNombre(Optional.ofNullable(sofkianoData.getSegundoNombre()))
                .primerApellido(sofkianoData.getPrimerApellido())
                .segundoApellido(Optional.ofNullable(sofkianoData.getSegundoApellido()))
                .direccion(sofkianoData.getDireccion())
                .fechaCreacion(sofkianoData.getFechaCreacion())
                .fechaActualizacion(sofkianoData.getFechaActualizacion())
                .fechaSalida(Optional.ofNullable(sofkianoData.getFechaSalida()))
                .activo(sofkianoData.isActivo())
                .cliente(Optional.ofNullable(ClienteMapper.toEntity(sofkianoData.getClienteData())))
                .build();
    }

    public static SofkianoData toData(Sofkiano sofkiano) {
        return SofkianoData.builder()
                .dni(sofkiano.getDni())
                .tipoIdentificacion(sofkiano.getTipoIdentificacion().name())
                .numeroIdentificacion(sofkiano.getNumeroIdentificacion())
                .primerNombre(sofkiano.getPrimerNombre())
                .segundoNombre(sofkiano.getSegundoNombre().orElse(null))
                .primerApellido(sofkiano.getPrimerApellido())
                .segundoApellido(sofkiano.getSegundoApellido().orElse(null))
                .direccion(sofkiano.getDireccion())
                .fechaCreacion(sofkiano.getFechaCreacion())
                .fechaActualizacion(sofkiano.getFechaActualizacion())
                .fechaSalida(sofkiano.getFechaSalida().orElse(null))
                .activo(sofkiano.isActivo())
                .clienteData(ClienteMapper.toData(sofkiano.getCliente().orElse(null)))
                .build();
    }
}
