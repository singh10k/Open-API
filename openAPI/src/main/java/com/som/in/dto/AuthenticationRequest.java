package com.som.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,20}$", message = "Username must be between 3 and 20 characters and contain only letters and numbers")
    private String username;
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,20}$", message = "Password must be between 8 and 20 characters and include at least one digit, one uppercase letter, one lowercase letter, and one special character")
    private String password;
}

