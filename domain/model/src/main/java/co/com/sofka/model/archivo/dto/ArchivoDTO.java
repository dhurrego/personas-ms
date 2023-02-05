package co.com.sofka.model.archivo.dto;

import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;

public record ArchivoDTO(String extension, Flux<ByteBuffer> contenidoBytes) {
}
