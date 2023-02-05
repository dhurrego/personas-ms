package co.com.sofka.jpa.sincronizacionmasiva;

import co.com.sofka.jpa.detallesincronizacion.DetalleSincronizacionData;
import co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sincronizaciones_masivas")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SincronizacionMasivaData {

    @Id
    private String idSincronizacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSincronizacion estado;

    @Column(nullable = false)
    private Integer ejecucionesExitosas;

    @Column(nullable = false)
    private Integer ejecucionesFallidas;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sincronizacionMasivaData", fetch = FetchType.EAGER)
    private List<DetalleSincronizacionData> detallesSincronizacionData;
}
