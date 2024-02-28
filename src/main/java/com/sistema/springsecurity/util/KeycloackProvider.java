package com.sistema.springsecurity.util;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.stereotype.Service;

@Service
public class KeycloackProvider {

    private static final String SERVER_URL="http://localhost:9090";
    private static final String REALM_NAME="test-spring-boot-realm-dev";
    private static final String REALM_MASTER="master";
    private static final String CLIENT_ID_REAL_MASTER="admin-cli";
    private static final String USER_CONSOLE="admin";
    private static final String PASSWORD_CONSOLE="admin";
    private static final String CLIENT_SECRET="";

    public static RealmResource getRealmResource(){
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM_MASTER)
                .clientId(CLIENT_ID_REAL_MASTER)
                .username(USER_CONSOLE)
                .password(PASSWORD_CONSOLE)
                .clientSecret(CLIENT_SECRET)
                .resteasyClient(new ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)
                        .build())
                .build();
        return keycloak.realm(REALM_NAME);
    }

    public static UsersResource getUserResource(){
        RealmResource realmResource=getRealmResource();
        return realmResource.users();
    }
}
