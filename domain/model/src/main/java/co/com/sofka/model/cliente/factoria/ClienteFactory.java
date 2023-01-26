package co.com.sofka.model.cliente.factoria;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.cliente.dto.ClienteDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static co.com.sofka.model.exception.negocio.BusinessException.Tipo.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteFactory {

    public static Cliente crearCliente(ClienteDTO clienteDTO) {
        validarCamposObligatorios(clienteDTO);
        validarFormatoNIT(clienteDTO.nit());

        return Cliente.builder()
                .nit(clienteDTO.nit())
                .razonSocial(clienteDTO.razonSocial())
                .build();
    }

    private static void validarCamposObligatorios(ClienteDTO clienteDTO) {
        if(Objects.isNull(clienteDTO.nit()) ||
                clienteDTO.nit().isEmpty()) {
            throw ERROR_NIT_REQUERIDO.build();
        }
        if(Objects.isNull(clienteDTO.razonSocial()) ||
                clienteDTO.razonSocial().isEmpty()) {
            throw ERROR_RAZON_SOCIAL_REQUERIDO.build();
        }
    }

    private static void validarFormatoNIT(String nit) {
        final Pattern regexNumerico = Pattern.compile("\\d+");

        if(nit.length() != 10) {
            throw ERROR_LONGITUD_NIT.build();
        }

        if(!regexNumerico.matcher(nit).matches()) {
            throw ERROR_FORMATO_NIT_INVALIDO.build();
        }

        validarNumeroVerificacionNIT(nit);
    }

    private static void validarNumeroVerificacionNIT(String nit) {
        List<Integer> numerosPrimos = Arrays.asList(41, 37, 29, 23, 19, 17, 13, 7, 3);

        List<String> nitSeparado = Arrays.stream(nit.split("")).toList();

        int sumaDigitos = IntStream.range(0, numerosPrimos.size())
                .map( index -> Integer.parseInt(nitSeparado.get(index)) * numerosPrimos.get(index) )
                .sum();

        int digitoVerificacion = sumaDigitos % 11;

        digitoVerificacion = digitoVerificacion > 1 ? 11 - digitoVerificacion : digitoVerificacion;

        if(digitoVerificacion != Integer.parseInt(nitSeparado.get(9))) {
            throw ERROR_DIGITO_VERIFICACION_NIT_INVALIDO.build();
        }
    }
}
