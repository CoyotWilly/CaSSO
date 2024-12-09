package com.coyotwilly.casso.models;

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
    private UUID id;

    @Column("name")
    private String name;

    @Column("age")
    @CassandraType(type = CassandraType.Name.VARINT)
    private int age;

    @Override
    public String toString() {
        return String.format("{ @type = %1$s, id = %2$s, name = %3$s, age = %4$d }", getClass().getName(), getId(),
                getName(), getAge());
    }
}
