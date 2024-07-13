package com.phucx.keycloakmanagement.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {
    @Value("${phucx.server-url}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPIConfig(){

        Server server = new Server();
        server.setDescription("Server Url in Development environment");
        server.setUrl(serverUrl);

        Contact contact = new Contact();
        contact.email("trongphuc22153@gmail.com");
        contact.setName("phucx");

        Info info = new Info();
        info.description("Keycloak REST API").contact(contact).version("1.0.0").title("Keycloak RestAPI");

        return new OpenAPI().info(info).servers(List.of(server));
    }
}
