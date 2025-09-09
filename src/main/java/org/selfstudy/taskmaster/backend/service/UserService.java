package org.selfstudy.taskmaster.backend.service;

import lombok.RequiredArgsConstructor;
import org.selfstudy.taskmaster.backend.exception.UserAlreadyExistsException;
import org.selfstudy.taskmaster.backend.exception.UserNotFoundException;
import org.selfstudy.taskmaster.backend.model.dto.request.CreateUserRequest;
import org.selfstudy.taskmaster.backend.model.entity.User;
import org.selfstudy.taskmaster.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User createUser(CreateUserRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists: " + request.getEmail());
        }

        String passwordHash = passwordEncoder.encode(request.getPassword());

        final User savedUser = userRepo.save(
            User.builder()
                .email(request.getEmail())
                .passwordHash(passwordHash)
                .build()
        );

        return savedUser;
    }

    public void deactivateUser(Long userId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

}
