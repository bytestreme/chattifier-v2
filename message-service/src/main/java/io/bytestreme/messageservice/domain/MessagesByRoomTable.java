package io.bytestreme.messageservice.domain;

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
@Table("messages_by_room")
public class MessagesByRoomTable {

    @PrimaryKeyColumn(name = "room_id", type = PrimaryKeyType.PARTITIONED)
    private UUID roomId;

    @PrimaryKeyColumn(name = "message_id", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID messageId;

    @Column("timestamp")
    private Instant timestamp;

    @Column("data")
    private String data;

    @Column("sender")
    private UUID sender;

    @Column("system_message")
    private Boolean systemMessage;

}
