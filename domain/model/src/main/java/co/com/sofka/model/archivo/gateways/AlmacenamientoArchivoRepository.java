package co.com.sofka.model.archivo.gateways;

import co.com.sofka.model.archivo.Archivo;
import reactor.core.publisher.Mono;

public interface AlmacenamientoArchivoRepository {

    Mono<Boolean> subir(Archivo archivoAlmacenamiento);
    Mono<byte[]> descargar(String nombreArchivo);

}
