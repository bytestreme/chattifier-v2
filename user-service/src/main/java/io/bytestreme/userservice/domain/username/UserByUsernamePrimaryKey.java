package io.bytestreme.userservice.domain.username;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.time.Instant;

@PrimaryKeyClass
public class UserByUsernamePrimaryKey {

    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED, name = "username")
    private String username;

    @PrimaryKeyColumn(
            ordinal = 1,
            type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING,
            name = "timestamp"
    )
    private Instant timestamp;
}
