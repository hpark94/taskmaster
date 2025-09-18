package org.selfstudy.taskmaster.entities.user.dto;

import java.time.LocalDateTime;

import org.selfstudy.taskmaster.entities.user.enums.UserStatus;

public record UserResponse(

    Long id,
    String email,
    UserStatus status,
    LocalDateTime createdAt,
    LocalDateTime lastModified

) {}
