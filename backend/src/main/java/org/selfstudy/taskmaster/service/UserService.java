package org.selfstudy.taskmaster.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.selfstudy.taskmaster.model.dto.request.CreateUserRequest;
import org.selfstudy.taskmaster.model.entity.User;
import org.selfstudy.taskmaster.model.enums.UserStatus;

public interface UserService {

    Optional<User> findUserById(Long id);

    Optional<User> findUserByEmail(String email);

    List<User> findAllUsers();

    List<User> findUsersByStatus(UserStatus status);

    List<User> findRecentUsers(LocalDateTime since);

    List<User> findRecentUsersByStatus(LocalDateTime since, UserStatus status);

    List<User> findUsersByMultipleStatus(List<UserStatus> status);

    boolean isEmailTaken(String email);

    User createUser(CreateUserRequest request);

    void updateUserStatus(Long userId, UserStatus status);
}
