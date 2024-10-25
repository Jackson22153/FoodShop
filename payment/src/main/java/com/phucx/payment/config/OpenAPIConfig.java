package com.phucx.payment.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {
    @Value("${phucx.server-url}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPIConfiguration(){
        OpenAPI openAPI = new OpenAPI();
        // contact
        Contact contact = new Contact();
        contact.setEmail("trongphuc22153@gmail.com");
        contact.setName("phucx");
        // info
        Info info = new Info();
        info.setContact(contact);
        info.version("1.0.0");
        info.setTitle("Payment API");
        info.setDescription("This is a payment service which is used for payment of foodshop");
        // server
        Server server = new Server();
        server.setUrl(serverUrl);
        server.setDescription("Server url in development environment");

        openAPI.info(info).servers(List.of(server))
            .components(new Components().addSecuritySchemes("bearAuth", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")))
            .security(List.of(new SecurityRequirement().addList("bearAuth")));
        return openAPI;
    }
}
