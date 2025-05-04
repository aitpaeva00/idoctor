package com.aitpaeva.idoctor.controller;

import com.aitpaeva.idoctor.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/token")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PostMapping("/refresh")
    public String refreshAccessToken(@RequestBody String refreshToken) {
        return tokenService.refreshAccessToken(refreshToken);
    }
}
