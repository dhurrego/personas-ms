package co.com.sofka.azure.config;

import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {

    @Value("${azure.storage.blob.connection-string}")
    private String connectionString;

    @Value("${azure.storage.blob.container}")
    private String container;

    @Bean
    public BlobContainerAsyncClient containerClient() {
        BlobServiceAsyncClient client = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildAsyncClient();
        return client.getBlobContainerAsyncClient(container);
    }
}
