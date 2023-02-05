package co.com.sofka.model.estadisticas.factoria;

import co.com.sofka.model.estadisticas.dto.HistorialCambioEstadoDTO;
import co.com.sofka.model.estadisticas.enums.TipoMovimiento;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HistorialCambioEstadoDTOFactory {

    public static HistorialCambioEstadoDTO crearAgregarHistorialCambioEstadoDTO(String dniSofkiano,
                                                                                String nombreCompletoSofkiano,
                                                                                TipoMovimiento tipoMovimiento) {
        return HistorialCambioEstadoDTO.builder()
                .dniSofkiano(dniSofkiano)
                .nombreCompletoSofkiano(nombreCompletoSofkiano)
                .tipoMovimiento(tipoMovimiento)
                .build();
    }
}
