package org.generalka.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        DataSource dataSource = DaoFactory.INSTANCE.getFullDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        userDao = new MysqlUserDao(jdbcTemplate);
    }

    @org.junit.jupiter.api.Test
    void testInsertUser() {
        // create user for testing
        User user = new User();
        user.setAdmin(false);
        user.setUsername("testUserss");
        user.setPassword("testPasswordss");
        // save user
        userDao.insertUser(user);

        Optional<User> retrievedUser = userDao.getUserByUsername("testUserss");


        assertTrue(retrievedUser.isPresent());
        assertEquals("testUserss", retrievedUser.get().getUsername());
        assertEquals("testPasswordss", retrievedUser.get().getPassword());
    }

    @org.junit.jupiter.api.Test
    void testGetUserByUsername() {
        // create user for testing
        User testUser = new User();
        testUser.setAdmin(false);
        testUser.setUsername("testGetUsers");
        testUser.setPassword("testPasswords");
        // save test
        userDao.insertUser(testUser);

        Optional<User> retrievedUser = userDao.getUserByUsername("testGetUsers");

        assertTrue(retrievedUser.isPresent());
        assertEquals("testGetUsers", retrievedUser.get().getUsername());
        assertEquals("testPasswords", retrievedUser.get().getPassword());
    }

    @org.junit.jupiter.api.Test
    void testGetUserByUsername_NotFound() {
        Optional<User> retrievedUser = userDao.getUserByUsername("nonExistentUser");
        assertTrue(retrievedUser.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void testGetCurrentUser() {
        // create user for testing
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setAdmin(true);
        currentUser.setUsername("current");
        currentUser.setPassword("currentPassword");
        userDao.setCurrentUser(currentUser);

        Optional<User> retrievedUser = userDao.getCurrentUser();

        assertTrue(retrievedUser.isPresent());
        assertEquals("current", retrievedUser.get().getUsername());
        assertEquals("currentPassword", retrievedUser.get().getPassword());
    }

    @org.junit.jupiter.api.Test
    void testGetCurrentUser_NotSet() {

        Optional<User> retrievedUser = userDao.getCurrentUser();
        assertTrue(retrievedUser.isEmpty());
    }

    @Test
    void testSetCurrentUser() {
        // set current user
        User currentUser = new User();
        currentUser.setId(2L);
        currentUser.setAdmin(false);
        currentUser.setUsername("newCurrent");
        currentUser.setPassword("newCurrentPassword");

        userDao.setCurrentUser(currentUser);

        Optional<User> retrievedUser = userDao.getCurrentUser();

        assertTrue(retrievedUser.isPresent());
        assertEquals("newCurrent", retrievedUser.get().getUsername());
        assertEquals("newCurrentPassword", retrievedUser.get().getPassword());
    }

    @Test
    void testGetUserIsAdminFromDatabase() {
        // crete user for testing
        User testUser = new User();
        testUser.setAdmin(true);
        testUser.setUsername("testGetUserIsAdmin");
        testUser.setPassword("testPassword");

        userDao.insertUser(testUser);

        // retrieve user id
        long userId = testUser.getId();

        // get isAdmin value from the database
        boolean isAdmin = userDao.getUserIsAdminFromDatabase(userId);

        // check if the isAdmin value retrieved successfully
        assertTrue(isAdmin);
    }

    @Test
    void testGetUserIsAdminFromDatabase_UserNotFound() {
        // attempt to retrieve isAdmin for a non-existent user
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.getUserIsAdminFromDatabase(999L));
    }

}