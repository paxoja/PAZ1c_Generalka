package org.generalka.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AnswerDaoTest {

    private AnswerDao answerDao;
    private TestDao testDao;
    private TestQuestionDao testQuestionDao;
    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        DataSource dataSource = DaoFactory.INSTANCE.getFullDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        answerDao = new MysqlAnswerDao(jdbcTemplate);
        testDao = new MysqlTestDao(jdbcTemplate);
        userDao = new MysqlUserDao(jdbcTemplate);
        testQuestionDao = new MysqlTestQuestionDao(jdbcTemplate);
    }

    @Test
    public void testSaveAnswer() {

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
        testQuestion.setQuestion("Sample Question");
        testQuestion.setTest(test);

        // save the test question
        testQuestionDao.saveTestQuestion(testQuestion);

        // create answer
        Answer answer = new Answer();
        answer.setAnswer("Sample Answer");
        answer.setIsCorrect(true);
        answer.setTestQuestion(testQuestion);

        // save answer
        answerDao.saveAnswer(answer);

        // retrieve saved answer
        Answer retrievedAnswer = answerDao.getAnswersByQuestionId(testQuestion.getId()).get(0);

        assertNotNull(retrievedAnswer);
        assertEquals("Sample Answer", retrievedAnswer.getAnswer());
        assertTrue(retrievedAnswer.getIsCorrect());
        assertEquals(testQuestion.getId(), retrievedAnswer.getTestQuestion().getId());
    }


    @Test
    void testGetAnswersByQuestionId() {
        // attempt to retrieve answers for non-existent question
        List<Answer> answers = answerDao.getAnswersByQuestionId(999L);
        assertTrue(answers.isEmpty());
    }

    @Test
    void testGetCorrectAnswerByQuestionId() {
        // create test for tesing
        org.generalka.storage.Test test = new org.generalka.storage.Test();
        test.setTopic("Test1");
        test.setIsWholeSemester(true);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        test.setDate(date);
        test.setSubject("UGR1");
        test.setSemester("winter");
        test.setYearOfStudy(2);

        // create user for testing
        User user = new User();
        user.setUsername("uniqueTestUser" + System.currentTimeMillis());
        user.setPassword("password");
        userDao.insertUser(user);
        test.setUser(user);

        testDao.saveTest(test);

        // create TestQuestion
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestion("What is the capital of Germany?");
        testQuestion.setTest(test);

        testQuestionDao.saveTestQuestion(testQuestion);

        // create answers
        Answer correctAnswer = new Answer();
        correctAnswer.setAnswer("Berlin");
        correctAnswer.setIsCorrect(true);
        correctAnswer.setTestQuestion(testQuestion);

        Answer incorrectAnswer = new Answer();
        incorrectAnswer.setAnswer("Paris");
        incorrectAnswer.setIsCorrect(false);
        incorrectAnswer.setTestQuestion(testQuestion);

        answerDao.saveAnswer(correctAnswer);
        answerDao.saveAnswer(incorrectAnswer);

        // get correct answer from the database
        Answer retrievedCorrectAnswer = answerDao.getCorrectAnswerByQuestionId(testQuestion.getId());

        assertNotNull(retrievedCorrectAnswer);
        assertEquals(correctAnswer, retrievedCorrectAnswer);
    }


    @Test
    void testGetCorrectAnswerByQuestionId_NoCorrectAnswer() {
        // attempt to get correct answer for question with no correct answer
        assertThrows(EmptyResultDataAccessException.class, () ->
                answerDao.getCorrectAnswerByQuestionId(999L));
    }
}
