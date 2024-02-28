package com.sistema.springsecurity.Service;

import com.sistema.springsecurity.controller.dto.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IkeycloakService {

    List<UserRepresentation> findAllUsers();
    List<UserRepresentation> searchUserByUsername(String username);
    String createUser(UserDTO userDTO);
    void deleteUser(String userId);
    void updateUser(String userId, UserDTO userDTO);

}
