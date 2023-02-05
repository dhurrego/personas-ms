package co.com.sofka.model.exception.tecnico;

import co.com.sofka.model.exception.BaseException;
import lombok.Getter;

public class TechnicalException extends BaseException {

    public enum Tipo {
        ERROR_COMUNICACION_BASE_DATOS("Error al intentar comunicarse con la base de datos"),
        ERROR_COMUNICACION_STORAGE_CLOUD("Error al intentar almacenar el archivo en la nube"),
        ERROR_DESCARGANDO_ARCHIVO_CLOUD("No se encontro el archivo a descargar del storage"),
        ERROR_PUBLICANDO_MENSAJE_SINCRONIZACION_MASIVA("Error al intentar solicitar la sincronizacion masiva de manera asincrona"),
        ERROR_PROCESANDO_ARCHIVO("Se produjo un error procesando el archivo");

        @Getter
        private final String message;

        Tipo(String message) {
            this.message = message;
        }

        public TechnicalException build() {
            return new TechnicalException(this);
        }
    }

    public TechnicalException(Tipo tipo) {
        super(tipo.getMessage(), 500);
    }

    public TechnicalException(String mensaje) {
        super(mensaje, 500);
    }
}
