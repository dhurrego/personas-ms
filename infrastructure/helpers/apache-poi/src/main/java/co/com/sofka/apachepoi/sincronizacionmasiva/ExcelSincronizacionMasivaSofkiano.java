package co.com.sofka.apachepoi.sincronizacionmasiva;

import co.com.sofka.model.sincronizacionmasiva.gateways.ExcelGateway;
import co.com.sofka.model.sofkiano.dto.SofkianoMasivoDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.IntStream;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_CANTIDAD_MAXIMA_REGISTROS_SINCRONIZACION_MASIVA_INVALIDO;
import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_PROCESANDO_ARCHIVO;

@Component
public class ExcelSincronizacionMasivaSofkiano implements ExcelGateway {

    private static final int NUMERO_COLUMNAS_PERMITIDAS = 9;
    private final DatosEntradaConfig datosEntradaConfig;

    @Value("${constantes.carga-masiva.registros.maximo}")
    private int maximoRegistrosCargaMasiva;

    public ExcelSincronizacionMasivaSofkiano() {
        datosEntradaConfig = new DatosEntradaConfig(NUMERO_COLUMNAS_PERMITIDAS);
    }

    @Override
    public Flux<SofkianoMasivoDTO> convertirExcelASofkiano(byte[] contenidoBytes) {
        return Mono.just(contenidoBytes)
                .map(ByteArrayInputStream::new)
                .flatMapMany( inputStream -> {
                    List<SofkianoMasivoDTO> sofkianosMasivoDTO = new ArrayList<>();

                    try {
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        XSSFSheet sheet = workbook.getSheetAt(0);

                        Iterator<Row> iteradorFilas = sheet.iterator();
                        iteradorFilas.next();


                        iteradorFilas.forEachRemaining( fila -> {
                            List<String> columnas = readColumns(fila);

                            if(!columnas.stream().allMatch(columna -> Objects.isNull(columna) ||
                                    columna.equals("") ||
                                    columna.equals(" ")))
                            {
                                sofkianosMasivoDTO.add(contruirSofkianoMasivoDTO(fila));
                            }
                        });
                    } catch (Exception e) {
                        throw ERROR_PROCESANDO_ARCHIVO.build();
                    }

                    validarCantidadRegistros(sofkianosMasivoDTO.size());
                    return Flux.fromIterable(sofkianosMasivoDTO);
                });
    }

    private SofkianoMasivoDTO contruirSofkianoMasivoDTO(Row fila) {
        List<String> columnas = readColumns(fila);

        return SofkianoMasivoDTO.builder()
                .numeroFila(fila.getRowNum() + 1)
                .tipoIdentificacion(columnas.get(0))
                .numeroIdentificacion(columnas.get(1))
                .primerNombre(columnas.get(2))
                .segundoNombre(Optional.ofNullable(columnas.get(3)))
                .primerApellido(columnas.get(4))
                .segundoApellido(Optional.ofNullable(columnas.get(5)))
                .direccion(columnas.get(6))
                .activo(columnas.get(7).equals("SI"))
                .nitCliente(Optional.ofNullable(columnas.get(8)))
                .build();
    }

    private void validarCantidadRegistros(int cantidadRegistros) {
        if(cantidadRegistros > maximoRegistrosCargaMasiva){
            throw ERROR_CANTIDAD_MAXIMA_REGISTROS_SINCRONIZACION_MASIVA_INVALIDO.build();
        }
    }

    private List<String> readColumns(Row fila) {
        List<String> columnas = new ArrayList<>();

        int ultimaColumna = Math.max(fila.getLastCellNum(), datosEntradaConfig.numeroColumnasPermitidas());

        IntStream.range(0, ultimaColumna)
                .forEach(index -> {
                    Cell celda = fila.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    if (celda.getCellType() == CellType.NUMERIC) {
                        columnas.add(NumberToTextConverter.toText(celda.getNumericCellValue()));
                    }

                    if (celda.getCellType() == CellType.STRING) {
                        columnas.add(celda.getStringCellValue());
                    }

                    if (celda.getCellType() == CellType.BLANK) {
                        columnas.add(null);
                    }
                });

        IntStream.range(columnas.size(), datosEntradaConfig.numeroColumnasPermitidas())
                .forEach(index -> columnas.add(null));

        return columnas;
    }
}
