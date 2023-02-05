package co.com.sofka.jpa.experiencia.data;

import co.com.sofka.jpa.cliente.ClienteData;
import co.com.sofka.jpa.sofkiano.data.SofkianoData;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "experiencias_sofkianos_cliente")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ExperienciaSofkianoClienteData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idExperiencia;

    @ManyToOne
    @JoinColumn(name = "dni_sofkiano", nullable = false, foreignKey = @ForeignKey(name = "FK_experiencia_sofkiano"))
    private SofkianoData sofkianoData;

    @ManyToOne
    @JoinColumn(name = "nit_cliente", nullable = false, foreignKey = @ForeignKey(name = "FK_experiencia_cliente"))
    private ClienteData clienteData;

    @Column(nullable = false)
    private Integer nivelSatisfaccion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
}
