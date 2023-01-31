package co.com.sofka.model.exception.negocio;

import co.com.sofka.model.exception.BaseException;
import lombok.Getter;

public class BusinessException extends BaseException {
    public enum Tipo {
        ERROR_SOFKIANO_ACTIVO("El sofkiano ya se encuentra activo", 400),
        ERROR_SOFKIANO_INACTIVO("El Sofkiano ya se encuentra inactivo", 400),
        ERROR_TIPO_IDENTIFICACION_REQUERIDO("El campo tipo de identificación es obligatorio", 400),
        ERROR_NUMERO_IDENTIFICACION_REQUERIDO("El campo número de identificación es obligatorio", 400),
        ERROR_PRIMER_NOMBRE_REQUERIDO("El campo primer nombre es obligatorio", 400),
        ERROR_PRIMER_APELLIDO_REQUERIDO("El campo primer apellido es obligatorio", 400),
        ERROR_DIRECCION_REQUERIDA("El campo dirección es obligatorio", 400),
        ERROR_TIPO_IDENTIFICACION_FORMATO_INVALIDO("El tipo de identificación ingresado no es valido", 400),
        ERROR_SOFKIANDO_YA_EXISTE("Ya existe un sofkiano registrado con el mismo DNI", 400),
        ERROR_CLIENTE_YA_EXISTE("Ya existe un cliente registrado con el mismo NIT", 400),
        ERROR_FORMATO_NUMERO_IDENTIFICACION_INVALIDO("El campo número identificación solo permite digitos (0-9)", 400),
        ERROR_LONGITUD_NUMERO_IDENTIFICACION("El campo número identificación solo permite un máximo de 10 caracteres", 400),
        ERROR_FORMATO_NOMBRE_INVALIDO("Alguno de los campos del nombre contiene caracteres no validos", 400),
        ERROR_NIT_REQUERIDO("El campo NIT es obligatorio", 400),
        ERROR_DNI_REQUERIDO("El campo DNI es obligatorio", 400),
        ERROR_RAZON_SOCIAL_REQUERIDO("El campo razon social es obligatorio", 400),
        ERROR_LONGITUD_NIT("El campo nit debe tener 10 dígitos incluido código de verificación", 400),
        ERROR_FORMATO_NIT_INVALIDO("El campo NIT solo permite digitos (0-9)", 400),
        ERROR_DIGITO_VERIFICACION_NIT_INVALIDO("El dígito de verificación del NIT no es valido", 400),
        ERROR_SOFKIANO_NO_ENCONTRADO("El sofkiano no se encuentra registrado", 404),
        ERROR_CLIENTE_NO_ENCONTRADO("El cliente no se encuentra registrado", 404),
        ERROR_CANTIDAD_MAXIMA_REGISTROS_SINCRONIZACION_MASIVA_INVALIDO("Se supero la cantidad máxima de registros permitida", 400),
        ERROR_PROCESO_SINCRONIZACION_MASIVA_NO_ENCONTRADO("No se encontro un proceso de sincronizacion masiva", 404),
        ERROR_ESTADO_INVALIDO_SINCRONIZACION_MASIVA("El estado del proceso de sincronizacion es diferente a CREADO", 400),
        ERROR_EXTENSION_ARCHIVO_INVALIDA("La extensión del archivo es invalida", 400);

        @Getter
        private final String message;

        @Getter
        private final int httpStatusCode;

        Tipo(String message, int httpStatusCode) {
            this.message = message;
            this.httpStatusCode = httpStatusCode;
        }

        public BusinessException build() {
            return new BusinessException(this);
        }
    }
    public BusinessException(Tipo tipo) {
        super(tipo.getMessage(), tipo.getHttpStatusCode());
    }
}