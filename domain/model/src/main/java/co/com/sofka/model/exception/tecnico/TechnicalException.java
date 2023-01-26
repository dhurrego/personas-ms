package co.com.sofka.model.exception.tecnico;

import co.com.sofka.model.exception.BaseException;
import lombok.Getter;

public class TechnicalException extends BaseException {

    public enum Tipo {
        ERROR_COMUNICACION_BASE_DATOS("Error al intentar comunicarse con la base de datos");

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
}
