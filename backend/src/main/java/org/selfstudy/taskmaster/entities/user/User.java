package org.selfstudy.taskmaster.entities.user;

import java.time.LocalDateTime;

import org.selfstudy.taskmaster.entities.user.dto.UserCreateRequest;
import org.selfstudy.taskmaster.entities.user.enums.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @PreUpdate
    protected void onUpdate() {
        lastModified = LocalDateTime.now();
    }

    protected User() {
    }

    protected User(
        Long id,
        String email,
        String passwordHash,
        LocalDateTime createdAt,
        LocalDateTime lastModified,
        UserStatus status
    ) {
        this.id           = id;
        this.email        = email;
        this.passwordHash = passwordHash;
        this.createdAt    = createdAt;
        this.lastModified = lastModified;
        this.status       = status;
    }

    public static User create(
        String email,
        String password,
        PasswordEncoder passwordEncoder
    ) {
        return new User(
            null,
            email,
            passwordEncoder.encode(password),
            LocalDateTime.now(),
            null,
            UserStatus.ACTIVE
        );
    }

    public static User createFromDto(
        UserCreateRequest request,
        PasswordEncoder passwordEncoder
    ) {
        return new User(
            null,
            request.email(),
            passwordEncoder.encode(request.password()),
            LocalDateTime.now(),
            null,
            UserStatus.ACTIVE
        );
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(password);
    }

    public void changeStatus(UserStatus status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", createdAt=" + createdAt +
               ", lastModified=" + lastModified + ", status=" + status + "]";
    }

}
