package com.onlineshop.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotEmpty(message = "Current password is required")
    private String currentPassword;
    @NotEmpty(message = "New password is required")
    private String newPassword;
} 