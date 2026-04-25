package com.yhj.mdeditor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * User Login Request DTO
 */
@Data
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}