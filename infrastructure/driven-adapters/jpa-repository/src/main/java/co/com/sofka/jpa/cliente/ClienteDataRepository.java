package co.com.sofka.jpa.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface ClienteDataRepository extends JpaRepository<ClienteData, String>, QueryByExampleExecutor<ClienteData> {
}
