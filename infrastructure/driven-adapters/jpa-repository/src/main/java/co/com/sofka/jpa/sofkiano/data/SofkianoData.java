package co.com.sofka.jpa.sofkiano.data;

import co.com.sofka.jpa.cliente.ClienteData;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sofkianos")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SofkianoData {

    @Id
    @Column(length = 23)
    private String dni;

    @Column(name = "tipo_identificacion", nullable = false, length = 3)
    private String tipoIdentificacion;

    @Column(name = "numero_identificacion", nullable = false, length = 20)
    private String numeroIdentificacion;

    @Column(name = "primer_nombre", nullable = false, length = 100)
    private String primerNombre;

    @Column(name = "segundo_nombre", length = 100)
    private String segundoNombre;

    @Column(name = "primer_apellido", nullable = false, length = 100)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 100)
    private String segundoApellido;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(name = "fecha_salida")
    private LocalDateTime fechaSalida;

    @ManyToOne
    @JoinColumn(name = "nit_cliente", foreignKey = @ForeignKey(name = "FK_sofkiano_cliente"))
    private ClienteData clienteData;
}
