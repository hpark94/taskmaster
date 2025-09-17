package org.selfstudy.taskmaster.model.dto.response;

import java.time.LocalDateTime;

import org.selfstudy.taskmaster.model.enums.UserStatus;

public record UserResponse(

    Long id,
    String email,
    UserStatus status,
    LocalDateTime createdAt,
    LocalDateTime lastModified

) {}
