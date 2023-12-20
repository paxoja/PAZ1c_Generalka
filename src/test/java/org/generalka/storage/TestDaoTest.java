package org.generalka.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class TestDaoTest {

    private org.generalka.storage.TestDao testDao;

    @BeforeEach
    void setUp() {
        DataSource dataSource = DaoFactory.INSTANCE.getFullDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        testDao = new MysqlTestDao(jdbcTemplate);
    }

    @Test
    void testSaveAndGetTest() throws EntityNotFoundException {
        // create user
        User user1 = new User();
        user1.setId(15L);
        user1.setUsername("testUser2");
        user1.setPassword("testPassword2");

        // create a test with a user
        org.generalka.storage.Test test = new org.generalka.storage.Test();
        test.setTopic("MZI");
        test.setIsWholeSemester(false);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        test.setDate(date);
        test.setSubject("MZI");
        test.setSemester("winter");
        test.setYearOfStudy(1);
        test.setUser(user1);

        // Save the test
        testDao.saveTest(test);

        // get the test by ID
        org.generalka.storage.Test retrievedTest = testDao.getTestById(test.getId());

        // tolerance of 1 sec
        long expectedMillis = date.getTime();
        long actualMillis = retrievedTest.getDate().getTime();
        long tolerance = 1000;  // 1 second tolerance

        // Assert other properties
        assertEquals("MZI", retrievedTest.getTopic());
        assertFalse(retrievedTest.getIsWholeSemester());

        // Compare timestamps with tolerance
        assertTrue(Math.abs(expectedMillis - actualMillis) <= tolerance);

        assertEquals("MZI", retrievedTest.getSubject());
        assertEquals("winter", retrievedTest.getSemester());
        assertEquals(1, retrievedTest.getYearOfStudy());
        assertNotNull(retrievedTest.getUser());
        assertEquals("testUser2", retrievedTest.getUser().getUsername());
        assertEquals("testPassword2", retrievedTest.getUser().getPassword());
    }



    @Test
    void testDeleteTest() throws EntityNotFoundException {
        // create a user
        User user2 = new User();
        user2.setId(4L);
        user2.setUsername("testUser");
        user2.setPassword("testPassword");

        // create a test with user
        org.generalka.storage.Test test = new org.generalka.storage.Test();
        test.setTopic("deleted test");
        test.setIsWholeSemester(true);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        test.setDate(date);
        test.setSubject("UGR1");
        test.setSemester("winter");
        test.setYearOfStudy(2);
        test.setUser(user2);

        // save test
        testDao.saveTest(test);

        // delete test
        testDao.deleteTest(test.getId());

        // when we want to open the deleted test it should throw exception
        assertThrows(EmptyResultDataAccessException.class, () -> testDao.getTestById(test.getId()));
    }

    @Test
    void testGetAllTests() throws EntityNotFoundException {
        // create users
        User user3 = new User();
        user3.setId(5L);
        user3.setUsername("user3");
        user3.setPassword("password3");

        User user4 = new User();
        user4.setId(6L);
        user4.setUsername("user4");
        user4.setPassword("password4");

        // tests with users
        testDao.saveTest(new org.generalka.storage.Test(9L, "SWI", true, new Timestamp(System.currentTimeMillis()), "SWI1a", "summer", 1, user3));
        testDao.saveTest(new org.generalka.storage.Test(10L, "NUM", false, new Timestamp(System.currentTimeMillis()), "NUM", "winter", 2, user4));

        // get all tests
        List<org.generalka.storage.Test> allTests = testDao.getAllTests();

        assertEquals(2, allTests.size());
    }
}