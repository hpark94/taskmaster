package org.selfstudy.taskmaster.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.selfstudy.taskmaster.model.entity.User;
import org.selfstudy.taskmaster.model.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private static final String ACTIVE_TEST_USER_EMAIL
        = "testUser@example.com";
    private static final String ACTIVE_TEST_USER_PASSWORD_HASH
        = "testUserPasswordHash";
    private static final UserStatus ACTIVE_TEST_USER_STATUS
        = UserStatus.ACTIVE;

    private static final String INACTIVE_TEST_USER_EMAIL
        = "inactiveTestUser@example.com";
    private static final String INACTIVE_TEST_USER_PASSWORD_HASH
        = "inactiveTestUserPasswordHash";
    private static final UserStatus INACTIVE_TEST_USER_STATUS
        = UserStatus.INACTIVE;

    private static final String NON_EXIST_EMAIL = "nonexisting@example.com";

    private User activeTestUser;
    private User inactiveTestUser;

    @BeforeEach
    void setup() {

        activeTestUser = new User();
        activeTestUser.setId(null);
        activeTestUser.setEmail(ACTIVE_TEST_USER_EMAIL);
        activeTestUser.setPasswordHash(ACTIVE_TEST_USER_PASSWORD_HASH);
        activeTestUser.setCreatedAt(LocalDateTime.now());
        activeTestUser.setLastModified(null);
        activeTestUser.setStatus(ACTIVE_TEST_USER_STATUS);

        inactiveTestUser = new User();
        inactiveTestUser.setId(null);
        inactiveTestUser.setEmail(INACTIVE_TEST_USER_EMAIL);
        inactiveTestUser.setPasswordHash(INACTIVE_TEST_USER_PASSWORD_HASH);
        inactiveTestUser.setCreatedAt(LocalDateTime.now());
        inactiveTestUser.setLastModified(null);
        inactiveTestUser.setStatus(INACTIVE_TEST_USER_STATUS);

    }

    @Test
    void testFindByIdShouldReturnUserWhenIdExists() {
        entityManager.persistAndFlush(activeTestUser);
        final long userId = activeTestUser.getId();

        final Optional<User> result = userRepository.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(
            ACTIVE_TEST_USER_EMAIL,
            result.get()
                .getEmail()
        );
    }

    @Test
    void testFindByIdShouldReturnEmptyWhenIdDoesNotExist() {
        entityManager.persistAndFlush(activeTestUser);
        final long userId = activeTestUser.getId() + 1L;

        final Optional<User> result = userRepository.findById(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByEmailShouldReturnUserWhenEmailExists() {
        entityManager.persistAndFlush(activeTestUser);

        final Optional<User> result = userRepository.findByEmail(ACTIVE_TEST_USER_EMAIL);

        assertTrue(result.isPresent());
        assertEquals(
            ACTIVE_TEST_USER_EMAIL,
            result.get()
                .getEmail()
        );
    }

    @Test
    void testFindByEmailShouldReturnEmptyWhenEmailDoesNotExist() {
        entityManager.persistAndFlush(activeTestUser);

        final Optional<User> result = userRepository.findByEmail(NON_EXIST_EMAIL);

        assertTrue(result.isEmpty());
    }

    @Test
    void testExistsByEmailShouldReturnTrueWhenEmailExists() {
        entityManager.persistAndFlush(activeTestUser);

        final boolean result = userRepository.existsByEmail(ACTIVE_TEST_USER_EMAIL);

        assertTrue(result);
    }

    @Test
    void testExistsByEmailShouldReturnFalseWhenEmailDoesNotExist() {
        entityManager.persistAndFlush(activeTestUser);

        final boolean result = userRepository.existsByEmail(NON_EXIST_EMAIL);

        assertFalse(result);
    }

    @Test
    void testFindByStatusShouldReturnUsersWhenStatusExists() {
        entityManager.persistAndFlush(activeTestUser);
        entityManager.persistAndFlush(activeTestUser);

        final List<User> usersWithStatus
            = userRepository.findByStatus(ACTIVE_TEST_USER_STATUS);

        assertEquals(1, usersWithStatus.size());
        assertEquals(activeTestUser, usersWithStatus.get(0));
    }

    @Test
    void testFindByStatusInShouldReturnUsersWhenStatusesExist() {
        entityManager.persistAndFlush(activeTestUser);
        entityManager.persistAndFlush(inactiveTestUser);

        final List<User> result
            = userRepository.findByStatusIn(
                Arrays.asList(ACTIVE_TEST_USER_STATUS, INACTIVE_TEST_USER_STATUS)
            );

        assertEquals(2, result.size());
        assertEquals(
            Arrays.asList(activeTestUser, inactiveTestUser),
            result
        );
    }

    @Test
    void testFindByCreatedAtAfterShouldReturnUsersRecentlyCreated() {
        final LocalDateTime since = LocalDateTime.now()
            .minusDays(7L);
        entityManager.persistFlushFind(activeTestUser);
        entityManager.persistFlushFind(inactiveTestUser);

        final List<User> result = userRepository.findByCreatedAtAfter(since);

        assertEquals(2, result.size());
    }

    @Test
    void testFindByCreatedAtAfterAndStatusShouldReturnUsersRecentlyCreatedWithStatus() {
        final LocalDateTime since = LocalDateTime.now()
            .minusDays(7L);
        entityManager.persistFlushFind(activeTestUser);
        entityManager.persistFlushFind(inactiveTestUser);

        final List<User> result = userRepository.findByCreatedAtAfterAndStatus(
            since,
            ACTIVE_TEST_USER_STATUS
        );

        assertEquals(1, result.size());
        assertEquals(activeTestUser, result.get(0));
    }

    @Test
    void testCountByStatusShouldReturnCountOfUsersWithStatus() {
        entityManager.persistFlushFind(activeTestUser);
        entityManager.persistFlushFind(inactiveTestUser);

        final long result = userRepository.countByStatus(
            ACTIVE_TEST_USER_STATUS
        );

        assertEquals(1, result);
    }

}
