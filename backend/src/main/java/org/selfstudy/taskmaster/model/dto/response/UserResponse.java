package org.selfstudy.taskmaster.model.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
}
