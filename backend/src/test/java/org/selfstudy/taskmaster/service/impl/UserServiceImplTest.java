package org.selfstudy.taskmaster.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.selfstudy.taskmaster.exception.UserNotFoundException;
import org.selfstudy.taskmaster.model.dto.request.CreateUserRequest;
import org.selfstudy.taskmaster.model.entity.User;
import org.selfstudy.taskmaster.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private CreateUserRequest createUserRequest;

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USER_EMAIL = "testUser@example.com";
    private static final String TEST_USER_PASSWORD_HASH = "testUserHash";
    private static final Boolean TEST_USER_IS_ACTIVE = true;

    private static final String CREATE_USER_REQUEST_EMAIL = "newUser@example.com";
    private static final String CREATE_USER_REQUEST_PASSWORD = "plainPassword";

    @BeforeEach
    void setup() {
        testUser = User.builder()
            .id(TEST_USER_ID)
            .email(TEST_USER_EMAIL)
            .passwordHash(TEST_USER_PASSWORD_HASH)
            .isActive(TEST_USER_IS_ACTIVE)
            .build();

        createUserRequest = CreateUserRequest.builder()
            .email(CREATE_USER_REQUEST_EMAIL)
            .password(CREATE_USER_REQUEST_PASSWORD)
            .build();
    }

    @Test
    void testFindUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepo.findById(TEST_USER_ID))
            .thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findUserById(TEST_USER_ID);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepo).findById(TEST_USER_ID);

    }

    @Test
    void testFindUserById_ShouldReturnEmpty_WhenUserDoesNotExist() {
        when(userRepo.findById(TEST_USER_ID))
            .thenReturn(Optional.empty());

        Optional<User> result = userService.findUserById(TEST_USER_ID);

        assertTrue(result.isEmpty());
        verify(userRepo).findById(TEST_USER_ID);
    }

    @Test
    void testFindUserByEmail_ShouldReturnUser_WhenUserExists() {
        when(userRepo.findByEmail(TEST_USER_EMAIL))
            .thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findUserByEmail(TEST_USER_EMAIL);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepo).findByEmail(TEST_USER_EMAIL);
    }

    @Test
    void testFindUserByEmail_ShouldReturnEmpty_WhenUserDoesNotExist() {
        when(userRepo.findByEmail(TEST_USER_EMAIL))
            .thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findUserByEmail(TEST_USER_EMAIL);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepo).findByEmail(TEST_USER_EMAIL);
    }

    @Test
    void testFindAllUsers_ShouldReturnAllUsers() {
        final String TEST_USER_2_EMAIL = "testUser2@example.com";
        final String TEST_USER_2_HASH = "testUser2Hash";

        User testUser2 = User.builder()
            .id(2L)
            .email(TEST_USER_2_EMAIL)
            .passwordHash(TEST_USER_2_HASH)
            .build();
        List<User> users = Arrays.asList(testUser, testUser2);
        when(userRepo.findAll()).thenReturn(users);

        List<User> result = userService.findAllUsers();

        assertEquals(2, result.size());
        assertEquals(users, result);
        verify(userRepo).findAll();
    }

    @Test
    void testCreateUser_ShouldCreateUser_WhenEmailDoesNotExist() {
        final String PASSWORD_HASH = "newUserHash";

        when(userRepo.existsByEmail(createUserRequest.getEmail()))
            .thenReturn(false);
        when(passwordEncoder.encode(createUserRequest.getPassword()))
            .thenReturn(PASSWORD_HASH);

        User expectedUser = User.builder()
            .email(createUserRequest.getEmail())
            .passwordHash(PASSWORD_HASH)
            .build();
        when(userRepo.save(any(User.class))).thenReturn(expectedUser);

        User result = userService.createUser(createUserRequest);

        assertNotNull(result);
        assertEquals(createUserRequest.getEmail(), result.getEmail());
        assertEquals(PASSWORD_HASH, result.getPasswordHash());
        assertEquals(expectedUser, result);

        verify(userRepo).existsByEmail(createUserRequest.getEmail());
        verify(passwordEncoder).encode(createUserRequest.getPassword());
        verify(userRepo).save(any(User.class));

    }

    @Test
    void testDeactivateUser_ShouldDeactivateUser_WhenUserExists() {
        final Long USER_ID = TEST_USER_ID;

        when(userRepo.findById(USER_ID)).thenReturn(Optional.of(testUser));
        when(userRepo.save(testUser)).thenReturn(testUser);

        userService.deactivateUser(USER_ID);

        assertFalse(testUser.getIsActive());
        verify(userRepo).findById(USER_ID);
        verify(userRepo).save(testUser);
    }

    @Test
    void testDeactivateUser_ShouldThrowException_WhenUserDoesNotExist() {
        final Long USER_ID = TEST_USER_ID;

        when(userRepo.findById(USER_ID)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> userService.deactivateUser(USER_ID)
        );

        assertEquals("User not found: " + USER_ID, exception.getMessage());
        verify(userRepo).findById(USER_ID);
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void testFindActiveUsers_ShouldReturnActiveUsers() {
        testUser.setIsActive(true);
        List<User> activeUsers = Arrays.asList(testUser);
        when(userRepo.findByIsActiveTrue()).thenReturn(activeUsers);

        List<User> result = userService.findActiveUsers();

        assertEquals(1, result.size());
        assertTrue(
            result.get(0)
                .getIsActive()
        );
        verify(userRepo).findByIsActiveTrue();
    }

    @Test
    void testFindRecentUsers_ShouldReturnRecentUsers() {
        LocalDateTime since = LocalDateTime.now()
            .minusDays(7L);
        List<User> recentUsers = Arrays.asList(testUser);
        when(userRepo.findRecentUsers(since)).thenReturn(recentUsers);

        List<User> result = userService.findRecentUsers(since);

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));

        verify(userRepo).findRecentUsers(since);
    }

    @Test
    void testIsEmailTaken_ShouldReturnTrue_WhenEmailExists() {
        final String email = "existing@example.com";
        when(userRepo.existsByEmail(email)).thenReturn(true);

        boolean result = userService.isEmailTaken(email);

        assertTrue(result);
        verify(userRepo).existsByEmail(email);
    }

    @Test
    void testIsEmailTaken_ShouldReturnFalse_WhenEmailDoesNotExist() {
        final String email = "existing@example.com";
        when(userRepo.existsByEmail(email)).thenReturn(false);

        boolean result = userService.isEmailTaken(email);

        assertFalse(result);
        verify(userRepo).existsByEmail(email);
    }
}
