package co.com.sofka.model.sofkiano.factoria;

import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.dto.SofkianoARegistrarDTO;
import co.com.sofka.model.sofkiano.dto.SofkianoMasivoDTO;
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
        validarCamposObligatorios(sofkianoARegistrarDTO.tipoIdentificacion(), sofkianoARegistrarDTO.numeroIdentificacion(),
                sofkianoARegistrarDTO.primerNombre(), sofkianoARegistrarDTO.primerApellido(),
                sofkianoARegistrarDTO.direccion());

        validarFormatoCampos(sofkianoARegistrarDTO.tipoIdentificacion(), sofkianoARegistrarDTO.numeroIdentificacion(),
                sofkianoARegistrarDTO.primerNombre(), sofkianoARegistrarDTO.segundoNombre(),
                sofkianoARegistrarDTO.primerApellido(), sofkianoARegistrarDTO.segundoApellido());

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

    public static Sofkiano crearSofkiano(SofkianoMasivoDTO sofkianoMasivoDTO) {
        validarCamposObligatorios(sofkianoMasivoDTO.tipoIdentificacion(), sofkianoMasivoDTO.numeroIdentificacion(),
                sofkianoMasivoDTO.primerNombre(), sofkianoMasivoDTO.primerApellido(), sofkianoMasivoDTO.direccion());

        validarFormatoCampos(sofkianoMasivoDTO.tipoIdentificacion(), sofkianoMasivoDTO.numeroIdentificacion(),
                sofkianoMasivoDTO.primerNombre(), sofkianoMasivoDTO.segundoNombre(),
                sofkianoMasivoDTO.primerApellido(), sofkianoMasivoDTO.segundoApellido());

        LocalDateTime fecha = LocalDateTime.now();
        return Sofkiano
                .builder()
                .dni(sofkianoMasivoDTO.tipoIdentificacion().concat(sofkianoMasivoDTO.numeroIdentificacion()))
                .tipoIdentificacion(TipoIdentificacion.valueOf(sofkianoMasivoDTO.tipoIdentificacion()))
                .numeroIdentificacion(sofkianoMasivoDTO.numeroIdentificacion())
                .primerNombre(sofkianoMasivoDTO.primerNombre())
                .segundoNombre(sofkianoMasivoDTO.segundoNombre())
                .primerApellido(sofkianoMasivoDTO.primerApellido())
                .segundoApellido(sofkianoMasivoDTO.segundoApellido())
                .direccion(sofkianoMasivoDTO.direccion())
                .activo(sofkianoMasivoDTO.activo())
                .fechaCreacion(fecha)
                .fechaActualizacion(fecha)
                .fechaSalida(Optional.empty())
                .cliente(Optional.empty())
                .build();
    }

    private static void validarCamposObligatorios(String tipoIdentificacion, String numeroIdentificacion,
                                                  String primerNombre, String primerApellido, String direccion) {
        if(validarSiCadenaEsNulaOVacia(tipoIdentificacion)) {
            throw ERROR_TIPO_IDENTIFICACION_REQUERIDO.build();
        }
        if(validarSiCadenaEsNulaOVacia(numeroIdentificacion)) {
            throw ERROR_NUMERO_IDENTIFICACION_REQUERIDO.build();
        }
        if(validarSiCadenaEsNulaOVacia(primerNombre)) {
            throw ERROR_PRIMER_NOMBRE_REQUERIDO.build();
        }
        if(validarSiCadenaEsNulaOVacia(primerApellido)) {
            throw ERROR_PRIMER_APELLIDO_REQUERIDO.build();
        }
        if(validarSiCadenaEsNulaOVacia(direccion)) {
            throw ERROR_DIRECCION_REQUERIDA.build();
        }
    }

    private static boolean validarSiCadenaEsNulaOVacia(String cadena) {
        return Objects.isNull(cadena) || cadena.isEmpty();
    }

    private static void validarFormatoCampos(String tipoIdentificacion, String numeroIdentificacion,
                                             String primerNombre, Optional<String> segundoNombre,
                                             String primerApellido, Optional<String> segundoApellido) {
        validarFormatoTipoIdentificacion(tipoIdentificacion);
        validarFormatoNumeroIdentificacion(numeroIdentificacion);
        validarFormatoNombre(primerNombre);
        segundoNombre.ifPresent(SofkianoFactory::validarFormatoNombre);
        validarFormatoNombre(primerApellido);
        segundoApellido.ifPresent(SofkianoFactory::validarFormatoNombre);
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
