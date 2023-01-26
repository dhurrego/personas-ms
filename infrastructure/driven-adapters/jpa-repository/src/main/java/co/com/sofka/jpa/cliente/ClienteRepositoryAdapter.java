package co.com.sofka.jpa.cliente;

import co.com.sofka.jpa.cliente.mapper.ClienteMapper;
import co.com.sofka.jpa.helper.AdapterOperations;
import co.com.sofka.model.cliente.Cliente;
import co.com.sofka.model.cliente.gateways.ClienteRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteRepositoryAdapter extends AdapterOperations<Cliente, ClienteData, String, ClienteDataRepository>
 implements ClienteRepository
{

    public ClienteRepositoryAdapter(ClienteDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Cliente.ClienteBuilder.class).build());
    }

    @Override
    protected ClienteData toData(Cliente entity) {
        return ClienteMapper.toData(entity);
    }

    @Override
    protected Cliente toEntity(ClienteData data) {
        return ClienteMapper.toEntity(data);
    }
}
