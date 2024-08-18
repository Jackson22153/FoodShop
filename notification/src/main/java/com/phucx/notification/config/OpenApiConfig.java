package com.phucx.notification.config;

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
    @Value("${phucx.server-dev-url}")
    private String serverDevUrl;

    @Bean
    public OpenAPI myOpenAPI(){
        Server serverUrl = new Server();
        serverUrl.setDescription("Server Url in Development environment");
        serverUrl.setUrl(serverDevUrl);

        Contact contact = new Contact();
        contact.setName("phucx");
        contact.setEmail("trongphuc22153@gmail.com");

        Info info = new Info();
        info.description("Notification API").contact(contact).version("1.0.0").title("Notification API");
        return new OpenAPI().info(info).servers(List.of(serverUrl));
    }
}
