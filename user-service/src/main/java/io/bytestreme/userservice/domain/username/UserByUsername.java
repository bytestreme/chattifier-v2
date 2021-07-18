package io.bytestreme.userservice.domain.username;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users_by_username")
public class UserByUsername {

    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED, name = "username")
    private String username;

    @PrimaryKeyColumn(
            ordinal = 1,
            type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING,
            name = "timestamp"
    )
    private Instant timestamp;

    @Column("user_id")
    private UUID userId;

}
