package org.selfstudy.taskmaster.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.selfstudy.taskmaster.entities.user.User;
import org.selfstudy.taskmaster.entities.user.dto.UserChangePasswordRequest;
import org.selfstudy.taskmaster.entities.user.dto.UserChangeStatusRequest;
import org.selfstudy.taskmaster.entities.user.dto.UserCreateRequest;
import org.selfstudy.taskmaster.entities.user.enums.UserStatus;
import org.selfstudy.taskmaster.exception.UserAlreadyExistsException;
import org.selfstudy.taskmaster.exception.UserNotFoundException;
import org.selfstudy.taskmaster.repository.UserRepository;
import org.selfstudy.taskmaster.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findUsersByStatus(UserStatus status) {
        return userRepository.findByStatus(status);
    }

    @Override
    public List<User> findRecentUsers(LocalDateTime since) {
        return userRepository.findByCreatedAtAfter(since);
    }

    @Override
    public List<User> findRecentUsersByStatus(
        LocalDateTime since,
        UserStatus status
    ) {
        return userRepository.findByCreatedAtAfterAndStatus(since, status);
    }

    @Override
    public List<User> findUsersByMultipleStatus(List<UserStatus> status) {
        return userRepository.findByStatusIn(status);
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public User createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(
                "User already exists with Email: " + request.email()
            );
        }

        return userRepository.save(
            User.createFromDto(request, passwordEncoder)
        );
    }

    @Override
    @Transactional
    public void changeUserPassword(UserChangePasswordRequest request) {
        User user = userRepository.findById(request.id())
            .orElseThrow(
                () -> new UserNotFoundException(
                    "User not found with ID: " + request.id()
                )
            );
        user.changePassword(request.password(), passwordEncoder);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changeUserStatus(UserChangeStatusRequest request) {
        User user = userRepository.findById(request.id())
            .orElseThrow(
                () -> new UserNotFoundException(
                    "User not found with ID: " + request.id()
                )
            );
        user.changeStatus(request.status());
        userRepository.save(user);
    }

}
