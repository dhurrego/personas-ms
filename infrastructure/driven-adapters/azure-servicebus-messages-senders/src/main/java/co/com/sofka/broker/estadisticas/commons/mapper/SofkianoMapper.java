package co.com.sofka.broker.estadisticas.commons.mapper;

import co.com.sofka.broker.estadisticas.commons.dto.SofkianoHistorialDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SofkianoMapper {

    public static SofkianoHistorialDTO crearSofkianoDTO(String dni, String nombre) {
        return new SofkianoHistorialDTO(dni, nombre);
    }
}
