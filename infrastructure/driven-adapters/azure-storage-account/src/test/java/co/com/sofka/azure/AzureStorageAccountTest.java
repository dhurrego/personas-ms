package co.com.sofka.azure;

import co.com.sofka.model.archivo.Archivo;
import co.com.sofka.model.exception.tecnico.TechnicalException;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.ByteBuffer;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_COMUNICACION_STORAGE_CLOUD;
import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_DESCARGANDO_ARCHIVO_CLOUD;

@SpringBootTest
@SpringBootConfiguration
@ExtendWith(MockitoExtension.class)
class AzureStorageAccountTest {

    private static final String NOMBRE_ARCHIVO = "archivo.xlsx";

    @InjectMocks
    private AzureStorageAccount azureStorageAccount;

    @Value("${azure.storage.blob.connection-string}")
    private String connectionString;

    @Value("${azure.storage.blob.container}")
    private String container;

    private Archivo archivo;

    @BeforeEach
    void setUp() {
        archivo = Archivo.builder()
                .nombreArchivo(NOMBRE_ARCHIVO)
                .extension("xlsx")
                .contenidoBytes(Flux.just(ByteBuffer.wrap(new byte[0])))
                .build();

        BlobServiceAsyncClient client = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildAsyncClient();

        ReflectionTestUtils.setField(azureStorageAccount,"blobContainerAsyncClient",
                client.getBlobContainerAsyncClient(container));
    }

    @Test
    void subirArchivoExitosamente() {
        StepVerifier.create(azureStorageAccount.subir(archivo))
                .assertNext(Assertions::assertTrue)
                .verifyComplete();
    }

    @Test
    void descargaExitosa() {
        StepVerifier.create(azureStorageAccount.descargar(NOMBRE_ARCHIVO))
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();
    }

    @Test
    void descargaFallidaArchivoNoExiste() {
        StepVerifier.create(azureStorageAccount.descargar("prueba"))
                .expectErrorMatches(throwable -> throwable instanceof TechnicalException &&
                        ERROR_DESCARGANDO_ARCHIVO_CLOUD.getMessage().equals(throwable.getMessage())
                ).verify();
    }

    @Test
    void descargaFallidaErrorComunicacionStorage() {
        BlobServiceAsyncClient client = new BlobServiceClientBuilder()
                .connectionString(connectionString.concat("-"))
                .buildAsyncClient();

        ReflectionTestUtils.setField(azureStorageAccount,"blobContainerAsyncClient",
                client.getBlobContainerAsyncClient(container));

        StepVerifier.create(azureStorageAccount.descargar("prueba"))
                .expectErrorMatches(throwable -> throwable instanceof TechnicalException &&
                        ERROR_COMUNICACION_STORAGE_CLOUD.getMessage().equals(throwable.getMessage())
                ).verify();
    }

    @Test
    void subirArchivFallidoErrorComunicacionStorage() {
        BlobServiceAsyncClient client = new BlobServiceClientBuilder()
                .connectionString(connectionString.concat("-"))
                .buildAsyncClient();

        ReflectionTestUtils.setField(azureStorageAccount,"blobContainerAsyncClient",
                client.getBlobContainerAsyncClient(container));

        StepVerifier.create(azureStorageAccount.subir(archivo))
                .expectErrorMatches(throwable -> throwable instanceof TechnicalException &&
                        ERROR_COMUNICACION_STORAGE_CLOUD.getMessage().equals(throwable.getMessage())
                ).verify();
    }
}