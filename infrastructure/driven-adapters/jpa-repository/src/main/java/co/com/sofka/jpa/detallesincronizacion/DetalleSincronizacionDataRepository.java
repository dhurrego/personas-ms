package co.com.sofka.jpa.detallesincronizacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface DetalleSincronizacionDataRepository extends JpaRepository<DetalleSincronizacionData, Integer>,
        QueryByExampleExecutor<DetalleSincronizacionData> {
}
