package io.bytestreme.messageservice.domain;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

public interface MessagesByRoomRepository extends ReactiveCassandraRepository<MessagesByRoomTable, MessagesByRoomKey> {
}
