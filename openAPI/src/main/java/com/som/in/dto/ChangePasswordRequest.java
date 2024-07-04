package com.som.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class ChangePasswordRequest {
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,20}$", message = "Password must be between 8 and 20 characters and include at least one digit, one uppercase letter, one lowercase letter, and one special character")
    private String currentPassword;
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,20}$", message = "Password must be between 8 and 20 characters and include at least one digit, one uppercase letter, one lowercase letter, and one special character")
    private String newPassword;
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,20}$", message = "Password must be between 8 and 20 characters and include at least one digit, one uppercase letter, one lowercase letter, and one special character")
    private String confirmationPassword;
}

