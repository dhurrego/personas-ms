package co.com.sofka.model.experiencia.factoria;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.experiencia.ExperienciaSofkianoCliente;
import co.com.sofka.model.experiencia.dto.RegistrarExperienciaSofkianoDTO;
import co.com.sofka.model.sofkiano.Sofkiano;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExperienciaSofkianoClienteFactory {

    public static ExperienciaSofkianoCliente crearExperienciaSofkianoCliente(
            RegistrarExperienciaSofkianoDTO registrarExperienciaSofkianoDTO) {

        validarCamposObligatorios(registrarExperienciaSofkianoDTO);
        validarDescripcion(registrarExperienciaSofkianoDTO.descripcion());
        validarNivelSatisfaccion(registrarExperienciaSofkianoDTO.nivelSatisfaccion());

        return ExperienciaSofkianoCliente.builder()
                .descripcion(registrarExperienciaSofkianoDTO.descripcion())
                .nivelSatisfaccion(registrarExperienciaSofkianoDTO.nivelSatisfaccion())
                .cliente(Cliente.builder().nit(registrarExperienciaSofkianoDTO.nitCliente()).build())
                .sofkiano(Sofkiano.builder().dni(registrarExperienciaSofkianoDTO.dniSofkiano()).build())
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    private static void validarCamposObligatorios(RegistrarExperienciaSofkianoDTO registrarExperienciaSofkianoDTO) {
        if(validarSiCadenaEsNulaOVacia(registrarExperienciaSofkianoDTO.dniSofkiano())) {
            throw ERROR_DNI_REQUERIDO.build();
        }

        if(validarSiCadenaEsNulaOVacia(registrarExperienciaSofkianoDTO.nitCliente())) {
            throw ERROR_NIT_REQUERIDO.build();
        }

        if(validarSiCadenaEsNulaOVacia(registrarExperienciaSofkianoDTO.descripcion())) {
            throw ERROR_DESCRIPCION_REQUERIDO.build();
        }

        if(Objects.isNull(registrarExperienciaSofkianoDTO.nivelSatisfaccion())) {
            throw ERROR_NIVEL_SATISFACCION_REQUERIDO.build();
        }
    }

    private static boolean validarSiCadenaEsNulaOVacia(String cadena) {
        return Objects.isNull(cadena) || cadena.isEmpty();
    }

    private static void validarDescripcion(String descripcion) {
        final Pattern regexDescripcion = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóú\\d ]*$");

        if(descripcion.length() <= 5) {
            throw ERROR_LONGITUD_DESCRIPCION.build();
        }

        if(!regexDescripcion.matcher(descripcion).matches()) {
            throw ERROR_FORMATO_DESCRIPCION_INVALIDO.build();
        }
    }

    private static void validarNivelSatisfaccion(Integer nivelSatisfaccion) {
        if(nivelSatisfaccion < 0 || nivelSatisfaccion > 10) {
            throw ERROR_NIVEL_SATISFACCION_VALOR_INVALIDO.build();
        }
    }
}
