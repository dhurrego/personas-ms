package co.com.sofka.azure;

import co.com.sofka.model.archivo.Archivo;
import co.com.sofka.model.archivo.gateways.AlmacenamientoArchivoRepository;
import co.com.sofka.model.exception.tecnico.TechnicalException;
import com.azure.core.util.FluxUtil;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobContainerAsyncClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_COMUNICACION_STORAGE_CLOUD;
import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_DESCARGANDO_ARCHIVO_CLOUD;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AzureStorageAccount implements AlmacenamientoArchivoRepository {

    private final BlobContainerAsyncClient blobContainerAsyncClient;

    @Override
    public Mono<Boolean> subir(Archivo archivoAlmacenamiento) {
        return almacenarArchivo(
                blobContainerAsyncClient.getBlobAsyncClient(archivoAlmacenamiento.nombreArchivo()),
                archivoAlmacenamiento
        );
    }

    @Override
    public Mono<byte[]> descargar(String nombreArchivo) {
        return descargarArchivoAlmacenado(blobContainerAsyncClient.getBlobAsyncClient(nombreArchivo));
    }

    private Mono<Boolean> almacenarArchivo(BlobAsyncClient blobAsyncClient, Archivo archivoAlmacenamiento) {
        return blobAsyncClient.upload(archivoAlmacenamiento.contenidoBytes(), null, true)
                .map(blockBlobItem -> Boolean.TRUE)
                .onErrorResume( throwable -> {
                    log.error(throwable.toString());
                    return Mono.error(ERROR_COMUNICACION_STORAGE_CLOUD::build);
                });
    }

    private Mono<byte[]> descargarArchivoAlmacenado(BlobAsyncClient blobAsyncClient){
        return blobAsyncClient.exists()
                .flatMap(existe -> {
                    if(Boolean.FALSE.equals(existe)) {
                        return Mono.error(ERROR_DESCARGANDO_ARCHIVO_CLOUD.build());
                    }

                    return FluxUtil.collectBytesInByteBufferStream(blobAsyncClient.downloadStream());
                })
                .onErrorResume( throwable -> {
                    log.error(throwable.toString());
                    if(throwable instanceof TechnicalException) {
                        return Mono.error(throwable);
                    }
                    return Mono.error(ERROR_COMUNICACION_STORAGE_CLOUD::build);
                });
    }

}
