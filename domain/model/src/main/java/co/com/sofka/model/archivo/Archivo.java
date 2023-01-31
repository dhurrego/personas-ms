package co.com.sofka.model.archivo;

import lombok.Builder;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;

@Builder()
public record Archivo(String nombreArchivo, String extension, Flux<ByteBuffer> contenidoBytes) {
}
