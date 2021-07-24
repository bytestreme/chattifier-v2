package io.bytestreme.userservice.domain.user;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UsersByIdRepository extends ReactiveCassandraRepository<UsersByIdTable, UUID> {
    Mono<UsersByIdTable> findByUsername(String username);
}
