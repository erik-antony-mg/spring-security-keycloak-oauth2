package com.sistema.springsecurity.Service.impl;


import com.sistema.springsecurity.Service.IkeycloakService;
import com.sistema.springsecurity.controller.dto.UserDTO;
import com.sistema.springsecurity.util.KeycloackProvider;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class IkeycloakServiceImpl implements IkeycloakService {
    @Override
    public List<UserRepresentation> findAllUsers() {
        return KeycloackProvider.getRealmResource()
                .users()
                .list();
    }

    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeycloackProvider.getRealmResource()
                .users()
                .searchByUsername(username,true);
    }

    @Override
    public String createUser(@NonNull UserDTO userDTO) {
        int status ;
        UsersResource userResource=KeycloackProvider.getUserResource();

        UserRepresentation userRepresentation=new UserRepresentation();
        userRepresentation.setFirstName(userDTO.firstName());
        userRepresentation.setLastName(userDTO.lastName());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        Response response=userResource.create(userRepresentation);
        status=response.getStatus();
        if (status==201){
            String path=response.getLocation().getPath();
            String userId=path.substring(path.lastIndexOf("/")+1);

            CredentialRepresentation  credentialRepresentation= new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userDTO.password());

            userResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource=KeycloackProvider.getRealmResource();

            List<RoleRepresentation> roleRepresentations;
            if (userDTO.roles()==null || userDTO.roles().isEmpty()){
                roleRepresentations = List.of(realmResource.roles().get("user").toRepresentation());
            } else {
                roleRepresentations=realmResource.roles()
                        .list()
                        .stream()
                        .filter(role -> userDTO.roles()
                                .stream()
                                .anyMatch(roleName-> roleName.equalsIgnoreCase(role.getName())))
                        .toList();
            }

            realmResource.users().get(userId).roles().realmLevel().add(roleRepresentations);
            return "usuario creado directamente";
        }
        else if (status==409){
            log.error("el usuario ya existe");
            return "usuario ya existe";
        }else {
            log.error("error al crear el usuario en keycloak");
            return "error al crear el usuario en keycloak";
        }

    }

    @Override
    public void deleteUser(String userId) {
        KeycloackProvider.getUserResource().get(userId).remove();
    }

    @Override
    public void updateUser(String userId, UserDTO userDTO) {

        CredentialRepresentation credentialRepresentation=new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.password());

        UserRepresentation userRepresentation=new UserRepresentation();
        userRepresentation.setFirstName(userDTO.firstName());
        userRepresentation.setLastName(userDTO.lastName());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource userResource =KeycloackProvider.getUserResource().get(userId);

        userResource.update(userRepresentation);

    }
}
