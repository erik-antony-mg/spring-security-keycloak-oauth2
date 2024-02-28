package com.sistema.springsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hola1")
    @PreAuthorize("hasRole('admin_client_role')")
    public String helloAdmin(){
        return "hello admin";
    }


    @GetMapping("/hola2")
    @PreAuthorize("hasRole('user_client_role') or  hasRole('admin_client_role')")
    public String helloUser(){
        return "hello user";
    }
}
