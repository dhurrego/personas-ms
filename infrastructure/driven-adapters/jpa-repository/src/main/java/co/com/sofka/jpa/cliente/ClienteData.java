package co.com.sofka.jpa.cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "clientes")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClienteData {

    @Id
    @Column(length = 23)
    private String nit;

    @Column(name = "razon_social", nullable = false, length = 200)
    private String razonSocial;
}
