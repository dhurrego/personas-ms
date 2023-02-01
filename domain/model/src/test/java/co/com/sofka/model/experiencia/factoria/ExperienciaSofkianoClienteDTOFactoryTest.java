package co.com.sofka.model.experiencia.factoria;

import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.experiencia.ExperienciaSofkianoCliente;
import co.com.sofka.model.experiencia.dto.ExperienciaSofkianoClienteDTO;
import co.com.sofka.model.sofkiano.Sofkiano;
import co.com.sofka.model.sofkiano.enums.TipoIdentificacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExperienciaSofkianoClienteDTOFactoryTest {

    private static final Integer ID_EXPERIENCIA = 1;
    private static final String STRING_TEST = "TESTING";
    private static final String TIPO_IDENTIFICACION = "CC";
    private static final String NUMERO_IDENTIFICACION = "123123123";
    private static final int NIVEL_SATISFACCION = 10;
    private static final String NIT = "8905625451";

    @Test
    void crearExperienciaSofkianoClienteDTO() {

        final String dni = TIPO_IDENTIFICACION.concat(NUMERO_IDENTIFICACION);

        Cliente cliente = Cliente.builder()
                .nit(NIT)
                .razonSocial(STRING_TEST)
                .build();

        Sofkiano sofkiano = Sofkiano.builder()
                .dni(dni)
                .tipoIdentificacion(TipoIdentificacion.valueOf(TIPO_IDENTIFICACION))
                .numeroIdentificacion(NUMERO_IDENTIFICACION)
                .primerNombre(STRING_TEST)
                .segundoNombre(Optional.of(STRING_TEST))
                .primerApellido(STRING_TEST)
                .segundoApellido(Optional.of(STRING_TEST))
                .activo(true)
                .direccion(STRING_TEST)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .fechaSalida(Optional.empty())
                .cliente(Optional.of(cliente))
                .build();

        ExperienciaSofkianoCliente experienciaSofkianoCliente = ExperienciaSofkianoCliente.builder()
                .idExperiencia(ID_EXPERIENCIA)
                .descripcion(STRING_TEST)
                .fechaCreacion(LocalDateTime.now())
                .sofkiano(sofkiano)
                .cliente(cliente)
                .nivelSatisfaccion(NIVEL_SATISFACCION)
                .build();

        ExperienciaSofkianoClienteDTO experienciaSofkianoClienteDTO = ExperienciaSofkianoClienteDTOFactory
                .crearExperienciaSofkianoClienteDTO(experienciaSofkianoCliente);

        assertEquals(NIT, experienciaSofkianoClienteDTO.nitCliente());
        assertEquals(dni, experienciaSofkianoClienteDTO.dniSofkiano());
        assertEquals(NIVEL_SATISFACCION, experienciaSofkianoClienteDTO.nivelSatisfaccion());
        assertEquals(STRING_TEST, experienciaSofkianoClienteDTO.descripcion());
    }
}