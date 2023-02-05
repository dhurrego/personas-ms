package co.com.sofka.model.cliente;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Cliente {
    private String nit;
    private String razonSocial;
}
