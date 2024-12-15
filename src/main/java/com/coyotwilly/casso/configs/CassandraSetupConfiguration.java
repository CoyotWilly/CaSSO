package com.coyotwilly.casso.configs;

import com.coyotwilly.casso.consts.Resources;
import com.coyotwilly.casso.enums.SchemaActionStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableCassandraRepositories
public class CassandraSetupConfiguration extends AbstractCassandraConfiguration {

    @Value("${casso.cassandra.contact-points:127.0.0.1:9042}")
    private List<String> contactPoints;

    @Value("${casso.cassandra.data-center:datacenter1}")
    private String datacenter;

    @Value("${casso.cassandra.keyspace:system}")
    private String keyspace;

    @Value("${casso.cassandra.dll-auto}")
    private SchemaActionStrategy schemaAction;

    @Override
    public CassandraConverter cassandraConverter() {
        CassandraConverter converter = super.cassandraConverter();
        MappingCassandraConverter mapping = new MappingCassandraConverter(converter.getMappingContext());

        mapping.setUserTypeResolver(new SimpleUserTypeResolver(getRequiredSession()));

        return mapping;
    }

    @Override
    protected String getLocalDataCenter() {
        if (datacenter == null || datacenter.isEmpty()) {
            return Objects.requireNonNull(super.getLocalDataCenter());
        }

        return datacenter;
    }

    @Override
    protected String getContactPoints() {
        if (contactPoints == null || contactPoints.isEmpty()) {
            return super.getContactPoints();
        } else if (contactPoints.size() > 1) {
            return contactPoints.getFirst();
        }

        return String.join(",", contactPoints);
    }

    @Override
    protected String getKeyspaceName() {
        if (datacenter == null || datacenter.isEmpty()) {
            return "system";
        }

        return keyspace;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification
                .createKeyspace(keyspace)
                .ifNotExists(true)
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withSimpleReplication();

        return List.of(specification);
    }

    @Override
    public SchemaAction getSchemaAction() {
        if (schemaAction == null) {
            return super.getSchemaAction();
        }

        return switch (schemaAction) {
            case CREATE -> SchemaAction.CREATE;
            case CREATE_UPDATE -> SchemaAction.CREATE_IF_NOT_EXISTS;
            case DROP -> SchemaAction.RECREATE;
            case DROP_CREATE -> SchemaAction.RECREATE_DROP_UNUSED;
            case IGNORE -> SchemaAction.NONE;
        };
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] { Resources.ENTITIES_PACKAGE };
    }


}
