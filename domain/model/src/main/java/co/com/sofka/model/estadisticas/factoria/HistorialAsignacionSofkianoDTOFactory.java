package co.com.sofka.model.estadisticas.factoria;

import co.com.sofka.model.estadisticas.dto.HistorialAsignacionSofkianoDTO;
import co.com.sofka.model.estadisticas.enums.TipoMovimiento;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HistorialAsignacionSofkianoDTOFactory {

    public static HistorialAsignacionSofkianoDTO crearAgregarHistorialAsignacionDTO(String dniSofkiano,
                                                                                    String nombreCompletoSofkiano,
                                                                                    String nitCliente,
                                                                                    String razonSocialCliente,
                                                                                    TipoMovimiento tipoMovimiento) {
        return HistorialAsignacionSofkianoDTO.builder()
                .dniSofkiano(dniSofkiano)
                .nombreCompletoSofkiano(nombreCompletoSofkiano)
                .nitCliente(nitCliente)
                .razonSocialCliente(razonSocialCliente)
                .tipoMovimiento(tipoMovimiento)
                .build();
    }
}
