package io.bytestreme.userservice.domain.username;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Mono;

public interface UserByUsernameRepository extends ReactiveCassandraRepository<UserByUsername, UserByUsernamePrimaryKey> {
    @Query("select username, user_id, timestamp from users_by_username limit 1")
    Mono<UserByUsername> findUserByUsernameUsername(String username);
}
