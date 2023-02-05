package co.com.sofka.jpa.detallesincronizacion;

import co.com.sofka.jpa.sincronizacionmasiva.SincronizacionMasivaData;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "detalles_sincronizaciones")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DetalleSincronizacionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_sincronizacion", nullable = false, foreignKey = @ForeignKey(name = "FK_detalle_sincronizacion"))
    private SincronizacionMasivaData sincronizacionMasivaData;

    @Column(nullable = false)
    private String descripcionFallido;
}
