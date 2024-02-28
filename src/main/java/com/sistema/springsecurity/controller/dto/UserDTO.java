package com.sistema.springsecurity.controller.dto;

import lombok.Builder;
import java.util.Set;



@Builder
public record UserDTO(
        String username,
        String email,
        String firstName,
        String lastName,
        String password,
        Set<String> roles) {

}
