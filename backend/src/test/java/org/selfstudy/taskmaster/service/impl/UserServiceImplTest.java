package org.selfstudy.taskmaster.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import org.selfstudy.taskmaster.exception.UserAlreadyExistsException;
import org.selfstudy.taskmaster.exception.UserNotFoundException;
import org.selfstudy.taskmaster.model.dto.request.CreateUserRequest;
import org.selfstudy.taskmaster.model.entity.User;
import org.selfstudy.taskmaster.model.enums.UserStatus;
import org.selfstudy.taskmaster.model.factory.UserFactory;
import org.selfstudy.taskmaster.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFactory userFactory;

    @InjectMocks
    private UserServiceImpl userService;

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USER_EMAIL = "testUser@example.com";
    private static final String TEST_USER_PASSWORD = "testUserPassword";
    private static final String TEST_USER_PASSWORD_HASH = "testUserPasswordHash";
    private static final UserStatus TEST_USER_STATUS = UserStatus.ACTIVE;

    private static final long TEST_USER_2_ID = 2L;
    private static final String TEST_USER_2_EMAIL = "testUser2@example.com";
    private static final String TEST_USER_2_PASSWORD_HASH = "testUser2PasswordHash";
    private static final UserStatus TEST_USER_2_STATUS = UserStatus.INACTIVE;

    private static User testUser;
    private static User testUser2;
    private static CreateUserRequest createUserRequest;

    @BeforeEach
    void setup() {

        testUser = new User();
        testUser.setId(TEST_USER_ID);
        testUser.setEmail(TEST_USER_EMAIL);
        testUser.setPasswordHash(TEST_USER_PASSWORD_HASH);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setLastModified(null);
        testUser.setStatus(TEST_USER_STATUS);

        testUser2 = new User();
        testUser2.setId(TEST_USER_2_ID);
        testUser2.setEmail(TEST_USER_2_EMAIL);
        testUser2.setPasswordHash(TEST_USER_2_PASSWORD_HASH);
        testUser2.setCreatedAt(LocalDateTime.now());
        testUser2.setLastModified(null);
        testUser2.setStatus(TEST_USER_2_STATUS);

        createUserRequest = new CreateUserRequest(
            TEST_USER_EMAIL,
            TEST_USER_PASSWORD
        );

    }

    @Test
    void testFindUserByIdShouldReturnUserWhenUserExists() {
        when(userRepository.findById(TEST_USER_ID))
            .thenReturn(Optional.of(testUser));

        final Optional<User> result = userService.findUserById(TEST_USER_ID);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());

        verify(userRepository).findById(TEST_USER_ID);
    }

    @Test
    void testFindUserByIdShouldReturnEmptyWhenUserDoesNotExist() {
        when(userRepository.findById(TEST_USER_ID))
            .thenReturn(Optional.empty());

        final Optional<User> result = userService.findUserById(TEST_USER_ID);

        assertTrue(result.isEmpty());

        verify(userRepository).findById(TEST_USER_ID);
    }

    @Test
    void testFindUserByEmailShouldReturnUserWhenUserExists() {
        when(userRepository.findByEmail(TEST_USER_EMAIL))
            .thenReturn(Optional.of(testUser));

        final Optional<User> result = userService.findUserByEmail(TEST_USER_EMAIL);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());

        verify(userRepository).findByEmail(TEST_USER_EMAIL);
    }

    @Test
    void testFindUserByEmailShouldReturnEmptyWhenUserDoesNotExist() {
        when(userRepository.findByEmail(TEST_USER_EMAIL))
            .thenReturn(Optional.of(testUser));

        final Optional<User> result = userService.findUserByEmail(TEST_USER_EMAIL);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());

        verify(userRepository).findByEmail(TEST_USER_EMAIL);
    }

    @Test
    void testFindAllUsersShouldReturnAllUsers() {

        final List<User> users = Arrays.asList(testUser, testUser2);

        when(userRepository.findAll()).thenReturn(users);

        final List<User> result = userService.findAllUsers();

        assertEquals(2, result.size());
        assertEquals(users, result);

        verify(userRepository).findAll();
    }

    @Test
    void testCreateUserShouldCreateUserWhenEmailDoesNotExist() {
        when(userRepository.existsByEmail(createUserRequest.email()))
            .thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userFactory.createFromDto(createUserRequest))
            .thenReturn(testUser);

        final User result = userService.createUser(createUserRequest);

        assertNotNull(result);
        assertEquals(createUserRequest.email(), result.getEmail());
        assertEquals(testUser, result);

        verify(userRepository).existsByEmail(createUserRequest.email());
        verify(userRepository).save(any(User.class));
        verify(userFactory).createFromDto(createUserRequest);
    }

    @Test
    void testCreateUserShouldThrowErrowWhenEmailExists() {
        when(userRepository.existsByEmail(createUserRequest.email()))
            .thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(
            UserAlreadyExistsException.class,
            () -> userService.createUser(createUserRequest)
        );

        assertEquals(
            "User already exists with Email: " + createUserRequest.email(),
            exception.getMessage()
        );

        verify(userRepository).existsByEmail(createUserRequest.email());
    }

    @Test
    void testUpdateUserStatusShouldUpdateStatusWhenUserExists() {
        final UserStatus status = UserStatus.INACTIVE;

        when(userRepository.findById(TEST_USER_ID))
            .thenReturn(Optional.of(testUser));

        userService.updateUserStatus(TEST_USER_ID, status);

        assertEquals(status, testUser.getStatus());

        verify(userRepository).findById(TEST_USER_ID);
    }

    @Test
    void testUpdateUserStatusShouldThrowErrorWhenUserDoesNotExist() {
        final Long userId = TEST_USER_ID;
        final UserStatus status = UserStatus.INACTIVE;

        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> userService.updateUserStatus(userId, status)
        );

        assertEquals(
            "User not found with ID: " + userId,
            exception.getMessage()
        );

        verify(userRepository).findById(userId);
    }

    @Test
    void testFindRecentUsersShouldReturnRecentUsers() {
        final LocalDateTime oneWeekAgo = LocalDateTime.now()
            .minusDays(7L);
        final List<User> recentUsers = Arrays.asList(testUser);

        when(userRepository.findByCreatedAtAfter(oneWeekAgo))
            .thenReturn(recentUsers);

        final List<User> result = userService.findRecentUsers(oneWeekAgo);

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));

        verify(userRepository).findByCreatedAtAfter(oneWeekAgo);
    }

    @Test
    void testIsEmailTakenShouldReturnTrueWhenEmailExists() {
        final String email = "existing@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        final boolean result = userService.isEmailTaken(email);

        assertTrue(result);
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void testIsEmailTakenShouldReturnFalseWhenEmailDoesNotExist() {
        final String email = "existing@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        final boolean result = userService.isEmailTaken(email);

        assertFalse(result);
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void testFindUsersByStatusShouldReturnUsersWithGivenStatus() {
        final UserStatus queryStatus = UserStatus.ACTIVE;
        final List<User> usersWithStatus = Arrays.asList(testUser);

        when(userRepository.findByStatus(queryStatus))
            .thenReturn(usersWithStatus);

        final List<User> result = userService.findUsersByStatus(queryStatus);

        assertEquals(1, result.size());
        assertEquals(usersWithStatus, result);

        verify(userRepository).findByStatus(queryStatus);
    }

    @Test
    void testFindUsersByMultipleStatusReturnUsersWithGivenStatuses() {
        final List<UserStatus> queryStatuses
            = Arrays.asList(UserStatus.ACTIVE, UserStatus.INACTIVE);
        final List<User> usersWithStatuses = Arrays.asList(testUser, testUser2);

        when(userRepository.findByStatusIn(queryStatuses))
            .thenReturn(usersWithStatuses);

        final List<User> result
            = userService.findUsersByMultipleStatus(queryStatuses);

        assertEquals(2, result.size());
        assertEquals(usersWithStatuses, result);

        verify(userRepository).findByStatusIn(queryStatuses);
    }

    @Test
    void testFindRecentUsersByStatus() {
        final LocalDateTime oneWeekAgo = LocalDateTime.now()
            .minusDays(7L);
        final UserStatus queryStatus = UserStatus.ACTIVE;
        final List<User> recentUsersWithStatus = Arrays.asList(testUser);

        when(
            userRepository
                .findByCreatedAtAfterAndStatus(oneWeekAgo, queryStatus)
        ).thenReturn(recentUsersWithStatus);

        final List<User> result
            = userService.findRecentUsersByStatus(oneWeekAgo, queryStatus);

        assertEquals(1, result.size());
        assertEquals(recentUsersWithStatus, result);

        verify(userRepository)
            .findByCreatedAtAfterAndStatus(oneWeekAgo, queryStatus);
    }

}
