package co.com.sofka.model.archivo.factoria;

import co.com.sofka.model.archivo.Archivo;
import co.com.sofka.model.archivo.dto.ArchivoDTO;
import co.com.sofka.model.exception.negocio.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.ByteBuffer;
import java.util.UUID;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_EXTENSION_ARCHIVO_INVALIDA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArchivoFactoryTest {

    private static final String ID_SINCRONIZACION = UUID.randomUUID().toString();
    private static final String EXTENSION = "xlsx";

    @Test
    void crearArchivoExitosamente() {
        ArchivoDTO archivoDTO = new ArchivoDTO(EXTENSION, Flux.just(ByteBuffer.wrap(new byte[0])));

        Archivo archivo = ArchivoFactory.crearArchivo(archivoDTO, ID_SINCRONIZACION);
        assertEquals(ID_SINCRONIZACION.concat(".").concat(EXTENSION), archivo.nombreArchivo());
        assertEquals(EXTENSION, archivo.extension());

        StepVerifier.create(archivo.contenidoBytes())
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();
    }

    @Test
    void crearArchivoFallidoPorExtensionInvalida() {
        ArchivoDTO archivoDTO = new ArchivoDTO("docx", Flux.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () ->  ArchivoFactory.crearArchivo(archivoDTO, ID_SINCRONIZACION));

        assertEquals(ERROR_EXTENSION_ARCHIVO_INVALIDA.getMessage(), exception.getMessage());
    }
}