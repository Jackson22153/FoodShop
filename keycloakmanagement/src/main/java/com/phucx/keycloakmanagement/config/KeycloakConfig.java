package com.phucx.keycloakmanagement.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${keycloak.server-url}")
    private String keycloakUrl;
    @Value("${keycloak.admin-username}")
    private String username;
    @Value("${keycloak.admin-password}")
    private String password;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.client-id}")
    private String clientId;

    @Bean
    public Keycloak keycloakInstance(){
        return KeycloakBuilder.builder()
            .serverUrl(keycloakUrl)
            .realm("master")
            .username(username)
            .password(password)
            .clientId("admin-cli")
            .build();
    }

    @Bean
    public RealmResource realmResource(){
        return keycloakInstance().realm(realm);
    }


}
