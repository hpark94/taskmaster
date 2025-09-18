package org.selfstudy.taskmaster.entities.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserChangePasswordRequest(

    @NotNull(message = "ID is required")
    Long id,

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password

) {}
