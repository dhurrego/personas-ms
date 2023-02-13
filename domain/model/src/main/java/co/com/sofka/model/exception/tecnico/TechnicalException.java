package co.com.sofka.model.exception.tecnico;

import co.com.sofka.model.exception.BaseException;
import lombok.Getter;

public class TechnicalException extends BaseException {

    public enum Tipo {
        ERROR_COMUNICACION_BASE_DATOS("Error al intentar comunicarse con la base de datos", 500),
        ERROR_COMUNICACION_STORAGE_CLOUD("Error al intentar almacenar el archivo en la nube", 500),
        ERROR_DESCARGANDO_ARCHIVO_CLOUD("No se encontro el archivo a descargar del storage", 500),
        ERROR_PUBLICANDO_MENSAJE_SINCRONIZACION_MASIVA("Error al intentar solicitar la sincronizacion masiva de manera asincrona", 500),
        ERROR_PROCESANDO_ARCHIVO("Se produjo un error procesando el archivo", 500),
        ACCESO_NO_AUTORIZADO("Acceso no autorizado", 401),
        TOKEN_INVALIDO("Token invalido o expirado", 401),
        ACCESO_NO_PERMITIDO("Acceso no permitido", 403);

        @Getter
        private final String message;

        @Getter
        private final Integer httpStatusCode;

        Tipo(String message, Integer httpStatusCode) {
            this.message = message;
            this.httpStatusCode = httpStatusCode;
        }

        public TechnicalException build() {
            return new TechnicalException(this);
        }
    }

    public TechnicalException(Tipo tipo) {
        super(tipo.getMessage(),tipo.getHttpStatusCode());
    }
}
