package com.aitpaeva.idoctor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {

    @GetMapping("/oauth2/success")
    public String success() {
        return "Social login successful!";
    }
}