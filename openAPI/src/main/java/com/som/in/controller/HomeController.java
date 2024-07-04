package com.som.in.controller;

import com.som.in.dto.ChangePasswordRequest;
import com.som.in.service.LoginService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@SecurityScheme(type = SecuritySchemeType.APIKEY, name = "Authorization", in = SecuritySchemeIn.HEADER)
public class HomeController {
    private final LoginService loginService;
    @PostMapping("/helloWord")
    @SecurityRequirement(name = "Authorization")
    public String helloWord(){
        return "Hello Word";
    }
    @SecurityRequirement(name = "Authorization")
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        loginService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
