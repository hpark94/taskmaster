package org.selfstudy.taskmaster.service;

import org.selfstudy.taskmaster.model.dto.request.CreateUserRequest;
import org.selfstudy.taskmaster.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findUserById(Long id);

    Optional<User> findUserByEmail(String email);

    List<User> findAllUsers();

    User createUser(CreateUserRequest request);

    void deactivateUser(Long userId);

    List<User> findActiveUsers();

    List<User> findRecentUsers(LocalDateTime since);

    boolean isEmailTaken(String email);
}
