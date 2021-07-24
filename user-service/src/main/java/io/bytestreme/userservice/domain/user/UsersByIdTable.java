package io.bytestreme.userservice.domain.user;

import lombok.*;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users_by_id")
public class UsersByIdTable {

    @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.PARTITIONED)
    private UUID userId;

    @Indexed("nameIndexForUsers")
    @Column("username")
    private String username;

    @Column("bio")
    private String bio;

    @Column("room_ids")
    private Set<UUID> roomIds;

    @Column("lastname")
    private String lastName;

    @Column("firstName")
    private String firstName;

    //password -> reserved cassandra keyword
    @Column("pass")
    private String pass;

}
