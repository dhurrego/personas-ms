package co.com.sofka.model.archivo.factoria;

import co.com.sofka.model.archivo.Archivo;
import co.com.sofka.model.archivo.dto.ArchivoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.ERROR_EXTENSION_ARCHIVO_INVALIDA;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArchivoFactory {

    private static final List<String> EXTENSIONES_VALIDAS = Collections.singletonList("xlsx");

    public static Archivo crearArchivo(ArchivoDTO archivoDTO, String idSincronizacion) {

        validarExtensionArchivo(archivoDTO.extension());

        return Archivo.builder()
                .nombreArchivo(idSincronizacion.concat(".").concat(archivoDTO.extension()))
                .extension(archivoDTO.extension())
                .contenidoBytes(archivoDTO.contenidoBytes())
                .build();
    }

    private static void validarExtensionArchivo(String extension) {
        if(EXTENSIONES_VALIDAS.stream().noneMatch( extensionValida -> extensionValida.equals(extension))) {
            throw ERROR_EXTENSION_ARCHIVO_INVALIDA.build();
        }
    }
}
