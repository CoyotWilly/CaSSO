package com.coyotwilly.casso.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @PrimaryKey
    @CassandraType(type = CassandraType.Name.TEXT)
    private String id;

    @Column("name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String name;

    @Column("login")
    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String login;

    @Column("password")
    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String password;

    @Column("is_locked")
    @CassandraType(type = CassandraType.Name.BOOLEAN)
    private Boolean isLocked;

    @Override
    public String toString() {
        return String.format("{ @type = %1$s, id = %2$s, name = %3$s, login = %4%s, password HAM-AC = %5%s, " +
                        "lock status = %6$b }", getClass().getName(), getId(), getName(), getLogin(), getPassword(),
                getIsLocked());
    }
}
