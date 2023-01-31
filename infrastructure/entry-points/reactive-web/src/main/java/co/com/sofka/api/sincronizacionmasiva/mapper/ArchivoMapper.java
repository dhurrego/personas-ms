package co.com.sofka.api.sincronizacionmasiva.mapper;

import co.com.sofka.model.archivo.dto.ArchivoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArchivoMapper {

    public static ArchivoDTO crearArchivoDTO(FilePart filePart) {
        return new ArchivoDTO(
                obtenerExtensionArchivo(filePart.filename()),
                filePart.content().map(DataBuffer::toByteBuffer)
        );
    }

    private static String obtenerExtensionArchivo(String nombreArchivo) {
        final String[] nombreArchivoSeparado = nombreArchivo.split("\\.");

        return Arrays.asList(nombreArchivoSeparado)
                .get(nombreArchivoSeparado.length - 1);
    }


}
