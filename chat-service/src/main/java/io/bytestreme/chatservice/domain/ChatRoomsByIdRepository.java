package io.bytestreme.chatservice.domain;


import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ChatRoomsByIdRepository extends ReactiveCassandraRepository<ChatRoomsByIdTable, UUID> {
    Mono<ChatRoomsByIdTable> findByRoomId(UUID roomId);
}
