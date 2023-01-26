package co.com.sofka.jpa.sofkiano;

import co.com.sofka.jpa.sofkiano.data.ConsolidadoAsignacionData;
import co.com.sofka.jpa.sofkiano.data.SofkianoData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

public interface SofkianoDataRepository extends JpaRepository<SofkianoData, String>, QueryByExampleExecutor<SofkianoData> {

    @Query(value = """
            SELECT new co.com.sofka.jpa.sofkiano.data.ConsolidadoAsignacionData(CASE 
            WHEN s.clienteData.nit IS NOT null 
            THEN 'CON_ASIGNACION' 
            ELSE 'SIN_ASIGNACION' 
            END, 
            COUNT(s.clienteData.nit)) 
            FROM SofkianoData s 
            WHERE s.activo = true 
            GROUP BY CASE WHEN s.clienteData.nit IS NOT null THEN 'CON_ASIGNACION' ELSE 'SIN_ASIGNACION' END""")
    List<ConsolidadoAsignacionData> obtenerConsolidadoAsignacion();
}
