package org.selfstudy.taskmaster.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.selfstudy.taskmaster.exception.UserAlreadyExistsException;
import org.selfstudy.taskmaster.exception.UserNotFoundException;
import org.selfstudy.taskmaster.model.dto.request.CreateUserRequest;
import org.selfstudy.taskmaster.model.entity.User;
import org.selfstudy.taskmaster.repository.UserRepository;
import org.selfstudy.taskmaster.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    @Override
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

    @Override
    public void deactivateUser(Long userId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found."));
        user.setIsActive(false);
        userRepo.save(user);
    }

    @Override
    public List<User> findActiveUsers() {
        return userRepo.findByIsActiveTrue();
    }

    @Override
    public List<User> findRecentUsers(LocalDateTime since) {
        return userRepo.findRecentUsers(since);
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userRepo.existsByEmail(email);
    }
}
