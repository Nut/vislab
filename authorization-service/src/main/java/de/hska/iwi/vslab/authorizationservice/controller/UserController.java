package de.hska.iwi.vslab.authorizationservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class UserController {

    @GetMapping("me")
    public Principal user(Principal principal) {
        return principal;
    }

}