package com.sistema.springsecurity.controller;


import com.sistema.springsecurity.Service.IkeycloakService;
import com.sistema.springsecurity.controller.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/keycloak/user")
@PreAuthorize("hasRole('admin_client_role')")
@RequiredArgsConstructor
public class KeycloakController {


    private final IkeycloakService ikeycloakService;

    @GetMapping("/list")
    public ResponseEntity<?> findAllUsers(){
        return ResponseEntity.ok(ikeycloakService.findAllUsers());
    }

    @GetMapping("/list/{username}")
    public ResponseEntity<?> findAllUsers(@PathVariable String username){
        return ResponseEntity.ok(ikeycloakService.searchUserByUsername(username));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) throws URISyntaxException {
        String response = ikeycloakService.createUser(userDTO);
        return ResponseEntity.created(new URI("/keycloack/user/create")).body(response);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO , @PathVariable String userId) {
       ikeycloakService.updateUser(userId,userDTO);
       return ResponseEntity.ok("Usuario actulizado correctamente");
    }

    @DeleteMapping("/delete/{userId}")
    public  ResponseEntity<?> deleteUser(@PathVariable String userId){
        ikeycloakService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
