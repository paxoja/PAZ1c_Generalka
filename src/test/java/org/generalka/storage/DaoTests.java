package org.generalka.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DaoTests {

    private UserDao userDao;


    @BeforeEach
    void setUp() {
        DataSource dataSource = DaoFactory.INSTANCE.getFullDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        userDao = new MysqlUserDao(jdbcTemplate);
    }

    @Test
    void testInsertUser() {
        // Create a test user
        User user = new User();
        user.setAdmin(false);
        user.setUsername("testUserss");
        user.setPassword("testPasswordss");

        // Insert the user
        userDao.insertUser(user);

        // Retrieve the user from the database
        Optional<User> retrievedUser = userDao.getUserByUsername("testUserss");

        // Check if the user was inserted successfully
        assertTrue(retrievedUser.isPresent());
        assertEquals("testUserss", retrievedUser.get().getUsername());
        assertEquals("testPasswordss", retrievedUser.get().getPassword());
    }

    @Test
    void testGetUserByUsername() {
        // Insert a user for testing
        User testUser = new User();
        testUser.setAdmin(false);
        testUser.setUsername("testGetUser");
        testUser.setPassword("testPassword");

        userDao.insertUser(testUser);

        // Retrieve the user by username
        Optional<User> retrievedUser = userDao.getUserByUsername("testGetUser");

        // Check if the user was retrieved successfully
        assertTrue(retrievedUser.isPresent());
        assertEquals("testGetUser", retrievedUser.get().getUsername());
        assertEquals("testPassword", retrievedUser.get().getPassword());
    }

    @Test
    void testGetUserByUsername_NotFound() {
        // Attempt to retrieve a non-existent user
        Optional<User> retrievedUser = userDao.getUserByUsername("nonExistentUser");

        // Check if the result is empty
        assertTrue(retrievedUser.isEmpty());
    }

    @Test
    void testGetCurrentUser() {
        // Set the current user
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setAdmin(true);
        currentUser.setUsername("current");
        currentUser.setPassword("currentPassword");

        userDao.setCurrentUser(currentUser);

        // Retrieve the current user
        Optional<User> retrievedUser = userDao.getCurrentUser();

        // Check if the current user was retrieved successfully
        assertTrue(retrievedUser.isPresent());
        assertEquals("current", retrievedUser.get().getUsername());
        assertEquals("currentPassword", retrievedUser.get().getPassword());
    }

    @Test
    void testGetCurrentUser_NotSet() {
        // Ensure that no current user is set
        Optional<User> retrievedUser = userDao.getCurrentUser();
        assertTrue(retrievedUser.isEmpty());
    }

    @Test
    void testSetCurrentUser() {
        // Set the current user
        User currentUser = new User();
        currentUser.setId(2L);
        currentUser.setAdmin(false);
        currentUser.setUsername("newCurrent");
        currentUser.setPassword("newCurrentPassword");

        userDao.setCurrentUser(currentUser);

        // Retrieve the current user
        Optional<User> retrievedUser = userDao.getCurrentUser();

        // Check if the current user was set and retrieved successfully
        assertTrue(retrievedUser.isPresent());
        assertEquals("newCurrent", retrievedUser.get().getUsername());
        assertEquals("newCurrentPassword", retrievedUser.get().getPassword());
    }
}
