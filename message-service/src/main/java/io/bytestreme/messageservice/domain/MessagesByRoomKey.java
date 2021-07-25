package io.bytestreme.messageservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyClass
public class MessagesByRoomKey {

    @PrimaryKeyColumn(name = "room_id", type = PrimaryKeyType.PARTITIONED)
    private UUID roomId;

    @PrimaryKeyColumn(name = "message_id", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID messageId;
}
