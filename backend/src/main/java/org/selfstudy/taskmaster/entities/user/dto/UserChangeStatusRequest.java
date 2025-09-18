package org.selfstudy.taskmaster.entities.user.dto;

import org.selfstudy.taskmaster.entities.user.enums.UserStatus;

import jakarta.validation.constraints.NotNull;

public record UserChangeStatusRequest(

    @NotNull(message = "ID is required")
    Long id,

    @NotNull(message = "Status is required")
    UserStatus status

) {}
