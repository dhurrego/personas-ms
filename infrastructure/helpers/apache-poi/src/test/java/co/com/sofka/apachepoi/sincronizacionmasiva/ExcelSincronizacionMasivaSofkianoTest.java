package co.com.sofka.apachepoi.sincronizacionmasiva;

import co.com.sofka.model.exception.negocio.BusinessException;
import co.com.sofka.model.exception.tecnico.TechnicalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.test.StepVerifier;

import java.io.IOException;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_CANTIDAD_MAXIMA_REGISTROS_SINCRONIZACION_MASIVA_INVALIDO;
import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_PROCESANDO_ARCHIVO;

@ExtendWith(MockitoExtension.class)
class ExcelSincronizacionMasivaSofkianoTest {

    @InjectMocks
    private ExcelSincronizacionMasivaSofkiano excelSincronizacionMasivaSofkiano;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(excelSincronizacionMasivaSofkiano, "maximoRegistrosCargaMasiva", 1);
    }

    @Test
    void convertirExcelASofkiano() throws IOException {
        StepVerifier.create(excelSincronizacionMasivaSofkiano
                        .convertirExcelASofkiano(new ClassPathResource("archivoPrueba.xlsx")
                                .getInputStream()
                                .readAllBytes())
                )
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void convertirExcelASofkianoFallidoArchivoVacio() {
        StepVerifier.create(excelSincronizacionMasivaSofkiano.convertirExcelASofkiano(new byte[0]))
                .expectError(TechnicalException.class)
                .verify();

        StepVerifier.create(excelSincronizacionMasivaSofkiano.convertirExcelASofkiano(new byte[0]))
                .expectErrorMessage(ERROR_PROCESANDO_ARCHIVO.getMessage())
                .verify();
    }

    @Test
    void convertirExcelASofkianoFallidoSuperaMaximoRegistros() throws IOException {
        ReflectionTestUtils.setField(excelSincronizacionMasivaSofkiano, "maximoRegistrosCargaMasiva", 0);

        StepVerifier.create(excelSincronizacionMasivaSofkiano
                        .convertirExcelASofkiano(new ClassPathResource("archivoPrueba.xlsx")
                                .getInputStream()
                                .readAllBytes())
                )
                .expectError(BusinessException.class)
                .verify();

        StepVerifier.create(excelSincronizacionMasivaSofkiano
                        .convertirExcelASofkiano(new ClassPathResource("archivoPrueba.xlsx")
                                .getInputStream()
                                .readAllBytes())
                )
                .expectErrorMessage(ERROR_CANTIDAD_MAXIMA_REGISTROS_SINCRONIZACION_MASIVA_INVALIDO.getMessage())
                .verify();
    }
}