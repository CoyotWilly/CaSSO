package com.coyotwilly.casso.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@Table("users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @PrimaryKey
    @Column("login")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String login;

    @Column("name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String name;

    @Column("id")
    @CassandraType(type = CassandraType.Name.TEXT)
    private UUID userId = UUID.randomUUID();

    @Column("password")
    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String password;

    @Override
    public String toString() {
        return String.format("{ @type = %1$s, id = %2$s, name = %3$s, login = %4$s, password HAM-AC = %5$s, }",
                getClass().getName(), getUserId(), getName(), getLogin(), getPassword());
    }
}
