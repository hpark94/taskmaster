package org.selfstudy.taskmaster.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
}
