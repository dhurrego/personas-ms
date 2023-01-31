package co.com.sofka.model.sincronizacionmasiva.gateways;

import co.com.sofka.model.sofkiano.dto.SofkianoMasivoDTO;
import reactor.core.publisher.Flux;

public interface ExcelGateway {
    Flux<SofkianoMasivoDTO> convertirExcelASofkiano(byte[] contenidoBytes);
}
