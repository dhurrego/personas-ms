package co.com.sofka.model.sofkiano.factoria;

import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.dto.SofkianoARegistrarDTO;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SofkianoFactory {

    public static Sofkiano crearSofkiano(SofkianoARegistrarDTO sofkianoARegistrarDTO) {
        validarCamposObligatorios(sofkianoARegistrarDTO);
        validarFormatoCampos(sofkianoARegistrarDTO);

        LocalDateTime fecha = LocalDateTime.now();
        return Sofkiano
                .builder()
                .dni(sofkianoARegistrarDTO.tipoIdentificacion().concat(sofkianoARegistrarDTO.numeroIdentificacion()))
                .tipoIdentificacion(TipoIdentificacion.valueOf(sofkianoARegistrarDTO.tipoIdentificacion()))
                .numeroIdentificacion(sofkianoARegistrarDTO.numeroIdentificacion())
                .primerNombre(sofkianoARegistrarDTO.primerNombre())
                .segundoNombre(sofkianoARegistrarDTO.segundoNombre())
                .primerApellido(sofkianoARegistrarDTO.primerApellido())
                .segundoApellido(sofkianoARegistrarDTO.segundoApellido())
                .direccion(sofkianoARegistrarDTO.direccion())
                .activo(true)
                .fechaCreacion(fecha)
                .fechaActualizacion(fecha)
                .fechaSalida(Optional.empty())
                .cliente(Optional.empty())
                .build();
    }

    private static void validarCamposObligatorios(SofkianoARegistrarDTO sofkianoARegistrarDTO) {
        if(validarSiCadenaEsNulaOVacia(sofkianoARegistrarDTO.tipoIdentificacion())) {
            throw ERROR_TIPO_IDENTIFICACION_REQUERIDO.build();
        }
        if(validarSiCadenaEsNulaOVacia(sofkianoARegistrarDTO.numeroIdentificacion())) {
            throw ERROR_NUMERO_IDENTIFICACION_REQUERIDO.build();
        }
        if(validarSiCadenaEsNulaOVacia(sofkianoARegistrarDTO.primerNombre())) {
            throw ERROR_PRIMER_NOMBRE_REQUERIDO.build();
        }
        if(validarSiCadenaEsNulaOVacia(sofkianoARegistrarDTO.primerApellido())) {
            throw ERROR_PRIMER_APELLIDO_REQUERIDO.build();
        }
        if(validarSiCadenaEsNulaOVacia(sofkianoARegistrarDTO.direccion())) {
            throw ERROR_DIRECCION_REQUERIDA.build();
        }
    }

    private static boolean validarSiCadenaEsNulaOVacia(String cadena) {
        return Objects.isNull(cadena) || cadena.isEmpty();
    }

    private static void validarFormatoCampos(SofkianoARegistrarDTO sofkianoARegistrarDTO) {
        validarFormatoTipoIdentificacion(sofkianoARegistrarDTO.tipoIdentificacion());
        validarFormatoNumeroIdentificacion(sofkianoARegistrarDTO.numeroIdentificacion());
        validarFormatoNombre(sofkianoARegistrarDTO.primerNombre());
        sofkianoARegistrarDTO.segundoNombre().ifPresent(SofkianoFactory::validarFormatoNombre);
        validarFormatoNombre(sofkianoARegistrarDTO.primerNombre());
        validarFormatoNombre(sofkianoARegistrarDTO.primerApellido());
        sofkianoARegistrarDTO.segundoApellido().ifPresent(SofkianoFactory::validarFormatoNombre);
    }

    private static void validarFormatoTipoIdentificacion(String tipoIdentificacion) {
        if(Arrays.stream(TipoIdentificacion.values()).noneMatch( tipo -> tipo.name().equals(tipoIdentificacion))) {
            throw ERROR_TIPO_IDENTIFICACION_FORMATO_INVALIDO.build();
        }
    }

    private static void validarFormatoNumeroIdentificacion(String numeroIdentificacion) {
        final Pattern regexNumerico = Pattern.compile("\\d+");

        if(numeroIdentificacion.length() > 10) {
            throw ERROR_LONGITUD_NUMERO_IDENTIFICACION.build();
        }

        if(!regexNumerico.matcher(numeroIdentificacion).matches()) {
            throw ERROR_FORMATO_NUMERO_IDENTIFICACION_INVALIDO.build();
        }
    }

    private static void validarFormatoNombre(String nombre) {
        final Pattern regexSoloLetras = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóú ]*$");

        if(!regexSoloLetras.matcher(nombre).matches()) {
            throw ERROR_FORMATO_NOMBRE_INVALIDO.build();
        }
    }

}
