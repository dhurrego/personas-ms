package co.com.sofka.jpa.sincronizacionmasiva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.transaction.annotation.Transactional;

public interface SincronizacionMasivaDataRepository extends JpaRepository<SincronizacionMasivaData, String>,
        QueryByExampleExecutor<SincronizacionMasivaData> {

    @Transactional
    @Modifying
    @Query(value = """
                    UPDATE SincronizacionMasivaData s
                    SET s.ejecucionesFallidas = s.ejecucionesFallidas + 1
                    WHERE s.idSincronizacion = :idSincronizacion""")
    Integer incrementarEjecucionesFallidas(@Param("idSincronizacion") String idSincronizacion);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE SincronizacionMasivaData s
            SET s.estado = CASE WHEN (s.ejecucionesFallidas = 0)
                            THEN co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion.FINALIZADO
                            ELSE co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion.FINALIZADO_CON_ERROR
                            END,
            s.ejecucionesExitosas = :ejecucionesExitosas
            WHERE s.idSincronizacion = :idSincronizacion""")
    Integer finalizarSincronizacionMasiva(@Param("idSincronizacion") String idSincronizacion,
                                          @Param("ejecucionesExitosas") Integer ejecucionesExitosas);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE SincronizacionMasivaData s
            SET s.estado = co.com.sofka.model.sincronizacionmasiva.EstadoSincronizacion.FINALIZADO_CON_ERROR
            WHERE s.idSincronizacion = :idSincronizacion""")
    Integer rechazarSincronizacionMasiva(@Param("idSincronizacion") String idSincronizacion);
}
