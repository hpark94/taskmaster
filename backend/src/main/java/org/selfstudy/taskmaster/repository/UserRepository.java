package org.selfstudy.taskmaster.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.selfstudy.taskmaster.entities.user.User;
import org.selfstudy.taskmaster.entities.user.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    List<User> findByStatus(UserStatus status);

    List<User> findByStatusIn(List<UserStatus> status);

    List<User> findByCreatedAtAfter(LocalDateTime date);

    List<User> findByCreatedAtAfterAndStatus(
        LocalDateTime date,
        UserStatus status
    );

    boolean existsByEmail(String email);

    long countByStatus(UserStatus status);

}
