package co.com.sofka.jpa.helper;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;
import java.util.function.Supplier;

import static co.com.sofka.model.exception.tecnico.TechnicalException.Tipo.ERROR_COMUNICACION_BASE_DATOS;
import static reactor.core.publisher.Mono.fromSupplier;

public abstract class AdapterOperations<E, D, I, R extends JpaRepository<D, I> & QueryByExampleExecutor<D>> {
    protected R repository;
    private final Class<D> dataClass;
    protected ObjectMapper mapper;
    private final Function<D, E> toEntityFn;

    protected AdapterOperations(R repository, ObjectMapper mapper, Function<D, E> toEntityFn) {
        this.repository = repository;
        this.mapper = mapper;
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.dataClass = (Class<D>) genericSuperclass.getActualTypeArguments()[1];
        this.toEntityFn = toEntityFn;
    }

    protected D toData(E entity) {
        return mapper.map(entity, dataClass);
    }

    protected E toEntity(D data) {
        return data != null ? toEntityFn.apply(data) : null;
    }

    public Mono<E> save(E entity) {
        return Mono.just(entity)
                .map(this::toData)
                .flatMap(this::saveData)
                .map(this::toEntity);
    }

    private Mono<E> doQuery(Mono<D> query) {
        return query.map(this::toEntity);
    }

    protected Mono<D> saveData(D data) {
        return fromSupplier(() -> repository.save(data))
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(throwable -> Mono.error(ERROR_COMUNICACION_BASE_DATOS::build));
    }

    public Mono<E> findById(I id) {
        return doQuery(fromSupplier(() -> repository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(Mono::justOrEmpty))
                .onErrorResume(throwable -> Mono.error(ERROR_COMUNICACION_BASE_DATOS::build));
    }

    public Flux<E> findAll() {
        return doQueryMany(() -> repository.findAll())
                .onErrorResume(throwable -> Mono.error(ERROR_COMUNICACION_BASE_DATOS::build));
    }

    protected Flux<E> doQueryMany(Supplier<Iterable<D>> query) {
        return fromSupplier(query).subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .map(this::toEntity)
                .onErrorResume(throwable -> Mono.error(ERROR_COMUNICACION_BASE_DATOS::build));
    }
}
