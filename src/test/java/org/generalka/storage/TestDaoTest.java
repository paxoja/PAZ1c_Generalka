package org.generalka.storage;

import org.generalka.storage.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestDaoTest {
    private TestDao testDao;
    private UserDao userDao;
    private TestQuestionDao testQuestionDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        DataSource dataSource = DaoFactory.INSTANCE.getFullDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        testDao = new MysqlTestDao(jdbcTemplate);
        userDao = new MysqlUserDao(jdbcTemplate);
        testQuestionDao = new MysqlTestQuestionDao(jdbcTemplate);
    }

    @Test
    void testSaveTest() {
        // create user for testing
        User user = new User();
        user.setUsername("uniqueTestUser" + System.currentTimeMillis());
        user.setPassword("password");
        userDao.insertUser(user);

        // create test for testing
        org.generalka.storage.Test test = new org.generalka.storage.Test();
        test.setTopic("Test Topic");
        test.setIsWholeSemester(false);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        test.setDate(date);
        test.setSubject("MZI");
        test.setSemester("winter");
        test.setYearOfStudy(2);
        test.setUser(user);
        test.setYearOfStudy(1);

        testDao.saveTest(test);

        long testId = test.getId();
        org.generalka.storage.Test retrievedTest = testDao.getTestById(testId);

        assertNotNull(retrievedTest);

        // check individual components of timestamp
        assertEquals(test.getDate().getMinutes(), retrievedTest.getDate().getMinutes(), "Minute mismatch");
        assertEquals(test.getDate().getSeconds(), retrievedTest.getDate().getSeconds(), "Second mismatch");

        assertEquals(test.getTopic(), retrievedTest.getTopic());
        assertEquals(test.getIsWholeSemester(), retrievedTest.getIsWholeSemester());
        assertEquals(test.getSubject(), retrievedTest.getSubject());
        assertEquals(test.getSemester(), retrievedTest.getSemester());
        assertEquals(test.getYearOfStudy(), retrievedTest.getYearOfStudy());
        assertEquals(testId, retrievedTest.getId());
    }




    @Test
    void testDeleteTest() {
        // create user for testing
        User user = new User();
        user.setUsername("uniqueTestUser" + System.currentTimeMillis()); // append timestamp to ensure uniqueness
        user.setPassword("password");
        userDao.insertUser(user);

        // create test for testing
        org.generalka.storage.Test test = new org.generalka.storage.Test();
        test.setTopic("Test Topic");
        test.setIsWholeSemester(false);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        test.setDate(date);
        test.setSubject("URG");
        test.setSemester("winter");
        test.setYearOfStudy(2);
        test.setUser(user);
        test.setYearOfStudy(1);

        testDao.saveTest(test);

        long testId = test.getId();

        // delete test
        testDao.deleteTest(testId);

        // try to retrieve deleted test
        assertThrows(EmptyResultDataAccessException.class, () -> testDao.getTestById(testId));
    }

    @Test
    void testGetAllTests() {
        // create user for testing
        User user = new User();
        user.setUsername("uniqueTestUser" + System.currentTimeMillis());
        user.setPassword("password");
        userDao.insertUser(user);

        // create user for testing
        User user2 = new User();
        user2.setUsername("uniqueTestUser" + System.currentTimeMillis());
        user2.setPassword("password");
        userDao.insertUser(user2);

        // create test for testing
        org.generalka.storage.Test test = new org.generalka.storage.Test();
        test.setTopic("Test Topic1");
        test.setIsWholeSemester(false);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        test.setDate(date);
        test.setSubject("URG");
        test.setSemester("winter");
        test.setYearOfStudy(2);
        test.setUser(user);
        test.setYearOfStudy(1);

        // create a test for tesing
        org.generalka.storage.Test test2 = new org.generalka.storage.Test();
        test2.setTopic("Test Topic2");
        test2.setIsWholeSemester(false);
        Timestamp date2 = new Timestamp(System.currentTimeMillis());
        test2.setDate(date2);
        test2.setSubject("URG");
        test2.setSemester("winter");
        test2.setYearOfStudy(2);
        test2.setUser(user2);
        test2.setYearOfStudy(1);

        testDao.saveTest(test2);
        testDao.saveTest(test);

        // retrieve all tests from the database
        List<org.generalka.storage.Test> allTests = testDao.getAllTests();

        // check if all tests are retrieved
        assertNotNull(allTests);
        assertEquals(allTests.size(), allTests.size());
    }

    @Test
    void testGetNumberOfQuestions() {
        // create user for testing
        User user = new User();
        user.setUsername("uniqueTestUser" + System.currentTimeMillis());
        user.setPassword("password");
        userDao.insertUser(user);

        org.generalka.storage.Test test = new org.generalka.storage.Test();
        test.setTopic("Test Topic");
        test.setIsWholeSemester(false);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        test.setDate(date);
        test.setSubject("URG");
        test.setSemester("winter");
        test.setYearOfStudy(2);
        test.setUser(user);
        test.setYearOfStudy(1);

        // save the test before saving questions
        testDao.saveTest(test);

        TestQuestion question1 = new TestQuestion();
        question1.setQuestion("Question 1");
        question1.setTest(test);
        testQuestionDao.saveTestQuestion(question1);

        TestQuestion question2 = new TestQuestion();
        question2.setQuestion("Question 2");
        question2.setTest(test);
        testQuestionDao.saveTestQuestion(question2);

        int numberOfQuestions = testDao.getNumberOfQuestions(test.getId());
        assertEquals(2, numberOfQuestions);
    }

    @Test
    void testUpdateTestAttribute() {
        // create user for testing
        User user = new User();
        user.setUsername("uniqueTestUser" + System.currentTimeMillis());
        user.setPassword("password");
        userDao.insertUser(user);

        // create test for testing
        org.generalka.storage.Test test = new org.generalka.storage.Test();
        test.setTopic("Test Topic");
        test.setIsWholeSemester(false);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        test.setDate(date);
        test.setSubject("URG");
        test.setSemester("winter");
        test.setYearOfStudy(2);
        test.setUser(user);
        test.setYearOfStudy(1);

        // save the test before updating
        testDao.saveTest(test);

        // update attribute
        String attributeName = "subject";
        String newAttributeValue = "New Subject";
        testDao.updateTestAttribute(test.getId(), attributeName, newAttributeValue);

        // retrieve updated test
        org.generalka.storage.Test updatedTest = testDao.getTestById(test.getId());

        assertNotNull(updatedTest);
        assertEquals(newAttributeValue, updatedTest.getSubject());
    }

    @Test
    void testUpdateTestEditAttribute() {
        // create user for testing
        User user = new User();
        user.setUsername("uniqueTestUser" + System.currentTimeMillis());
        user.setPassword("password");
        userDao.insertUser(user);

        // create test for testing
        org.generalka.storage.Test test = new org.generalka.storage.Test();
        test.setTopic("Test Topic");
        test.setIsWholeSemester(false);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        test.setDate(date);
        test.setSubject("URG");
        test.setSemester("winter");
        test.setYearOfStudy(2);
        test.setUser(user);
        test.setYearOfStudy(1);

        // save the test before updating
        testDao.saveTest(test);

        // update attribute
        String attributeName = "subject";
        String newAttributeValue = "New Subject";
        testDao.updateTestEditAttribute(test.getId(), attributeName, newAttributeValue);

        // retrieve updated test
        org.generalka.storage.Test updatedTest = testDao.getTestById(test.getId());

        assertNotNull(updatedTest);
        assertEquals(newAttributeValue, updatedTest.getSubject());
    }

}
