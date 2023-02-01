package co.com.sofka.jpa.experiencia;

import co.com.sofka.jpa.experiencia.data.ExperienciaSofkianoClienteData;
import co.com.sofka.jpa.experiencia.data.PromedioExperienciaClienteData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.time.LocalDate;
import java.util.List;

public interface ExperienciaSofkianoClienteDataRepository extends JpaRepository<ExperienciaSofkianoClienteData, Integer>,
        QueryByExampleExecutor<ExperienciaSofkianoClienteData> {
    List<ExperienciaSofkianoClienteData> findBySofkianoDataDniOrderByFechaCreacionDesc(String dniSofkiano);
    List<ExperienciaSofkianoClienteData> findBySofkianoDataDniAndClienteDataNitOrderByFechaCreacionDesc(String dniSofkiano, String nitCliente);
    List<ExperienciaSofkianoClienteData> findByClienteDataNitOrderByFechaCreacionDesc(String nitCliente);

    @Query(value = """
        SELECT new co.com.sofka.jpa.experiencia.data.PromedioExperienciaClienteData(
            c.nit,
            CASE WHEN AVG(e.nivelSatisfaccion) IS NOT NULL
                THEN AVG(e.nivelSatisfaccion)
                ELSE 0.0D
            END)
        FROM ClienteData c
        LEFT JOIN ExperienciaSofkianoClienteData e ON e.clienteData.nit = c.nit
        WHERE c.nit = :dniCliente AND ((DATE(e.fechaCreacion) BETWEEN :fechaInicial AND :fechaFinal) OR e.fechaCreacion IS NULL)
        GROUP BY c.nit""")
    PromedioExperienciaClienteData calcularPromedioCliente(@Param("dniCliente") String dniCliente,
                                                           @Param("fechaInicial") LocalDate fechaInicial,
                                                           @Param("fechaFinal") LocalDate fechaFinal);

    @Query(value = """
        SELECT new co.com.sofka.jpa.experiencia.data.PromedioExperienciaClienteData(
           c.nit,
            CASE WHEN AVG(e.nivelSatisfaccion) IS NOT NULL
                THEN AVG(e.nivelSatisfaccion)
                ELSE 0.0D
            END)
        FROM ClienteData c
        LEFT JOIN ExperienciaSofkianoClienteData e ON e.clienteData.nit = c.nit
        WHERE (DATE(e.fechaCreacion) BETWEEN :fechaInicial AND :fechaFinal) OR e.fechaCreacion IS NULL
        GROUP BY c.nit""")
    List<PromedioExperienciaClienteData> calcularPromedioCalificaciones(@Param("fechaInicial") LocalDate fechaInicial,
                                                                        @Param("fechaFinal") LocalDate fechaFinal);
}
