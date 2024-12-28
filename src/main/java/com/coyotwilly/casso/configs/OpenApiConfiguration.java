package com.coyotwilly.casso.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI defineOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Casso API")
                        .description("Cassandra based SSO system")
                        .contact(new Contact().name("Coyot Willy").url("https://github.com/coyotwilly/casso"))
                        .version("1.0"))
                .servers(defineServers());
    }

    private List<Server> defineServers() {
        List<Server> servers = new ArrayList<>();
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.description("Demo server");

        servers.add(server);

        return servers;
    }
}
