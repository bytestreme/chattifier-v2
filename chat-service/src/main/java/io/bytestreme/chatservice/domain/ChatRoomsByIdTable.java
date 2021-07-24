package io.bytestreme.chatservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Table("chat_rooms_by_id")
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomsByIdTable {

    @PrimaryKeyColumn(name = "room_id", type = PrimaryKeyType.PARTITIONED)
    private UUID roomId;

    @Column("room_name")
    private String roomName;

    @Column("creation_date")
    private Instant creationDate;

    @Column("creator_uid")
    private UUID creatorUid;

    @Column("participants")
    @CassandraType(type = CassandraType.Name.SET, typeArguments = CassandraType.Name.UUID)
    private Set<UUID> participants;

    @Column("banner")
    private String banner;

    @Column("one_to_one")
    private Boolean oneToOne;

}
