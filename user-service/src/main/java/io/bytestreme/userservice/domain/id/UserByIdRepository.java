package io.bytestreme.userservice.domain.id;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserByIdRepository extends ReactiveCassandraRepository<UserById, UserByIdPrimaryKey> {
    Mono<UserById> findByUserId(UUID uuid);
}
