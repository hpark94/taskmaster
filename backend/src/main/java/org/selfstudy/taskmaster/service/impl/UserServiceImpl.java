package org.selfstudy.taskmaster.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.selfstudy.taskmaster.exception.UserAlreadyExistsException;
import org.selfstudy.taskmaster.exception.UserNotFoundException;
import org.selfstudy.taskmaster.model.dto.request.CreateUserRequest;
import org.selfstudy.taskmaster.model.entity.User;
import org.selfstudy.taskmaster.model.enums.UserStatus;
import org.selfstudy.taskmaster.model.factory.UserFactory;
import org.selfstudy.taskmaster.repository.UserRepository;
import org.selfstudy.taskmaster.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserFactory userFactory;

    public UserServiceImpl(
        UserRepository userRepository,
        UserFactory userFactory
    ) {
        this.userRepository = userRepository;
        this.userFactory    = userFactory;
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
    public List<User> findRecentUsersByStatus(LocalDateTime since, UserStatus status) {
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
    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(
                "User already exists with Email: " + request.email()
            );
        }

        return userRepository.save(
            userFactory.createFromDto(request)
        );
    }

    @Override
    public void updateUserStatus(Long id, UserStatus status) {
        User user = userRepository.findById(id)
            .orElseThrow(
                () -> new UserNotFoundException("User not found with ID: " + id)
            );
        user.setStatus(status);
        userRepository.save(user);
    }

}
