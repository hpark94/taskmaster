package org.selfstudy.taskmaster.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.selfstudy.taskmaster.entities.user.User;
import org.selfstudy.taskmaster.entities.user.dto.UserChangePasswordRequest;
import org.selfstudy.taskmaster.entities.user.dto.UserChangeStatusRequest;
import org.selfstudy.taskmaster.entities.user.dto.UserCreateRequest;
import org.selfstudy.taskmaster.entities.user.enums.UserStatus;

public interface UserService {

    Optional<User> findUserById(Long id);

    Optional<User> findUserByEmail(String email);

    List<User> findAllUsers();

    List<User> findUsersByStatus(UserStatus status);

    List<User> findRecentUsers(LocalDateTime since);

    List<User> findRecentUsersByStatus(LocalDateTime since, UserStatus status);

    List<User> findUsersByMultipleStatus(List<UserStatus> status);

    boolean isEmailTaken(String email);

    User createUser(UserCreateRequest request);

    void changeUserPassword(UserChangePasswordRequest request);

    void changeUserStatus(UserChangeStatusRequest request);

}
