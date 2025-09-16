package org.selfstudy.taskmaster.model.dto.response;

import java.time.LocalDateTime;

import org.selfstudy.taskmaster.model.enums.UserStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record UserResponse(

    Long id,
    String email,
    UserStatus status,
    LocalDateTime createdAt,
    LocalDateTime lastModified

) {};
