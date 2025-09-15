package org.selfstudy.taskmaster.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.selfstudy.taskmaster.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private static final String TEST_USER_EMAIL = "testUser@example.com";
    private static final String TEST_USER_PASSWORD_HASH = "testUserHash";
    private static final Boolean TEST_USER_IS_ACTIVE = true;

    private static final String NON_EXIST_EMAIL = "nonexisting@example.com";

    private User activeTestUser;

    @BeforeEach
    void setup() {
        activeTestUser = User.builder()
            .email(TEST_USER_EMAIL)
            .passwordHash(TEST_USER_PASSWORD_HASH)
            .isActive(TEST_USER_IS_ACTIVE)
            .build();
    }

    @Test
    void testfindByEmail_ShouldReturnUser_WhenEmailExists() {
        entityManager.persistAndFlush(activeTestUser);

        Optional<User> found = userRepository.findByEmail(TEST_USER_EMAIL);

        assertTrue(found.isPresent());
        assertEquals(
            TEST_USER_EMAIL,
            found.get()
                .getEmail()
        );
    }

    @Test
    void testfindByEmail_ShouldReturnEmpty_WhenEmailDoesNotExist() {
        entityManager.persistAndFlush(activeTestUser);

        Optional<User> found = userRepository.findByEmail(NON_EXIST_EMAIL);

        assertTrue(found.isEmpty());
    }

    @Test
    void testExistsByEmail_ShouldReturnTrue_WhenEmailExists() {
        entityManager.persistAndFlush(activeTestUser);

        boolean result = userRepository.existsByEmail(TEST_USER_EMAIL);

        assertTrue(result);
    }

    @Test
    void testExistsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        entityManager.persistAndFlush(activeTestUser);

        boolean result = userRepository.existsByEmail(NON_EXIST_EMAIL);

        assertFalse(result);
    }

    void testfindRecentUsers_ShouldReturnRecentUsers() {
        LocalDateTime since = LocalDateTime.now()
            .minusDays(7L);
        entityManager.persistFlushFind(activeTestUser);

        List<User> recentUsers = userRepository.findRecentUsers(since);

        assertEquals(1, recentUsers.size());
    }

    void testfindByIsActiveTrue_ShouldReturnOnlyActiveUsers() {
        final String INACTIVE_EMAIL = "inactive@example.com";
        final String INACTIVE_PASSWORD_HASH = "inactiveHash";
        final boolean INACTIVE_STATUS = false;

        User inactiveUser = User.builder()
            .email(INACTIVE_EMAIL)
            .passwordHash(INACTIVE_PASSWORD_HASH)
            .isActive(INACTIVE_STATUS)
            .build();

        entityManager.persistAndFlush(activeTestUser);
        entityManager.persistAndFlush(inactiveUser);

        List<User> recentUsers = userRepository.findByIsActiveTrue();

        assertEquals(1, recentUsers.size());
        assertEquals(
            TEST_USER_EMAIL,
            recentUsers.get(0)
                .getEmail()
        );
    }

}
