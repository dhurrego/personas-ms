package co.com.sofka.model.sofkiano.factoria;

import co.com.sofka.model.sofkiano.AsignarClienteASofkiano;
import co.com.sofka.model.sofkiano.dto.AsignarClienteDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_DNI_REQUERIDO;
import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_NIT_REQUERIDO;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AsignarClienteASofkianoFactory {

    public static AsignarClienteASofkiano crearAsignarClienteASofkiano(AsignarClienteDTO asignarClienteDTO) {
        validarCamposObligatorios(asignarClienteDTO);

        return new AsignarClienteASofkiano(asignarClienteDTO.dniSofkiano(), asignarClienteDTO.nitCliente());
    }

    private static void validarCamposObligatorios(AsignarClienteDTO asignarClienteDTO) {
        if(Objects.isNull(asignarClienteDTO.dniSofkiano()) ||
                asignarClienteDTO.dniSofkiano().isEmpty()) {
            throw ERROR_DNI_REQUERIDO.build();
        }
        if(Objects.isNull(asignarClienteDTO.nitCliente()) ||
                asignarClienteDTO.nitCliente().isEmpty()) {
            throw ERROR_NIT_REQUERIDO.build();
        }
    }
}
