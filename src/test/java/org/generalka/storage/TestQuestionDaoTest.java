package org.generalka.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestQuestionDaoTest {

    private TestQuestionDao testQuestionDao;
    private TestDao testDao;
    private UserDao userDao;
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
    void testSaveAndGetTestQuestion() {
        // create user for testing
        User user = new User();
        user.setUsername("uniqueTestUser" + System.currentTimeMillis());
        user.setPassword("password");
        userDao.insertUser(user);

        // create test for testing
        org.generalka.storage.Test test = new org.generalka.storage.Test();
        test.setTopic("Test1");
        test.setIsWholeSemester(true);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        test.setDate(date);
        test.setSubject("UGR1");
        test.setSemester("winter");
        test.setYearOfStudy(2);
        test.setUser(user);
        testDao.saveTest(test);

        // create test question
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestion("What is your favorite color?");
        testQuestion.setTest(test);

        // save test question
        testQuestionDao.saveTestQuestion(testQuestion);

        // get saved test question by id
        Long testQuestionId = testQuestion.getId();
        TestQuestion retrievedTestQuestion = testQuestionDao.getTestQuestionById(testQuestionId);

        // check if the retrieved test question matches the saved one
        assertNotNull(retrievedTestQuestion);
        assertEquals(testQuestion.getQuestion(), retrievedTestQuestion.getQuestion());
        assertEquals(test.getId(), retrievedTestQuestion.getTest().getId());
    }

    @Test
    void testGetTestQuestionsByTestId() {
        // create user for testing
        User user = new User();
        user.setUsername("uniqueTestUser" + System.currentTimeMillis());
        user.setPassword("password");
        userDao.insertUser(user);

        // create a test for tesing
        org.generalka.storage.Test test = new org.generalka.storage.Test();
        test.setTopic("Test1");
        test.setIsWholeSemester(true);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        test.setDate(date);
        test.setSubject("UGR1");
        test.setSemester("winter");
        test.setYearOfStudy(2);
        test.setUser(user);
        testDao.saveTest(test);

        // Create test questions
        TestQuestion question1 = new TestQuestion();
        question1.setQuestion("Question 1");
        question1.setTest(test);
        testQuestionDao.saveTestQuestion(question1);

        TestQuestion question2 = new TestQuestion();
        question2.setQuestion("Question 2");
        question2.setTest(test);
        testQuestionDao.saveTestQuestion(question2);

        // get test questions by test id
        Long testId = test.getId();
        List<TestQuestion> testQuestions = testQuestionDao.getTestQuestionsByTestId(testId);

        // check if the correct number of test questions is retrieved
        assertEquals(2, testQuestions.size());

        // check if the retrieved test questions match the saved ones
        assertTrue(testQuestions.stream().anyMatch(q -> q.getId().equals(question1.getId())));
        assertTrue(testQuestions.stream().anyMatch(q -> q.getId().equals(question2.getId())));
    }

    @Test
    void testGetTestQuestionById_InvalidId() {
        // try to retrieve a test question with an invalid id
        assertThrows(EmptyResultDataAccessException.class, () -> testQuestionDao.getTestQuestionById(999L));
    }
}
