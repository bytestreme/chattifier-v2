package io.bytestreme.userservice.domain.id;

import com.datastax.oss.driver.api.core.type.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users_by_id")
public class UserById {

    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED, name = "user_id")
    private UUID userId;

    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @Column("permissions")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = { CassandraType.Name.FLOAT } )
    private List<String> permissions;

}
