package org.selfstudy.taskmaster.model.factory;

import java.time.LocalDateTime;

import org.selfstudy.taskmaster.model.dto.request.CreateUserRequest;
import org.selfstudy.taskmaster.model.entity.User;
import org.selfstudy.taskmaster.model.enums.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public UserFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User create(String email, String password) {
        final String passwordHash = passwordEncoder.encode(password);

        return new User(
            null,
            email,
            passwordHash,
            LocalDateTime.now(),
            null,
            UserStatus.ACTIVE
        );
    }

    public User createFromDto(CreateUserRequest request) {
        final String passwordHash = passwordEncoder.encode(request.password());

        return new User(
            null,
            request.email(),
            passwordHash,
            LocalDateTime.now(),
            null,
            UserStatus.ACTIVE
        );
    }

}
