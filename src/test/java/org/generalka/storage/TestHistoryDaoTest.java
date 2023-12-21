package org.generalka.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class TestHistoryDaoTest {

    private TestHistoryDao testHistoryDao;
    private TestDao testDao;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        DataSource dataSource = DaoFactory.INSTANCE.getFullDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        testHistoryDao = new MysqlTestHistoryDao(jdbcTemplate);
        testDao = new MysqlTestDao(jdbcTemplate);
        userDao = new MysqlUserDao(jdbcTemplate);
    }

    @Test
    void testSaveAndGetTestHistory() {
        // create user for testing
        User user = new User();
        user.setUsername("uniqueTestUser" + System.currentTimeMillis()); // append timestamp to ensure uniqueness
        user.setPassword("password");
        userDao.insertUser(user);

        /// create a test for tesing
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

        // create a test history
        TestHistory testHistory = new TestHistory();
        testHistory.setScore(90);
        testHistory.setReport("Good job");
        testHistory.setTest(test);
        testHistory.setUser(user);

        // save test history
        testHistoryDao.saveTestHistory(testHistory);

        // retrieve test history
        List<TestHistory> retrievedHistories = testHistoryDao.getTestHistoryByUserId(user.getId());

        assertNotNull(retrievedHistories);
        assertFalse(retrievedHistories.isEmpty());
        assertEquals(1, retrievedHistories.size());
        assertEquals(90, retrievedHistories.get(0).getScore());
        assertEquals("Good job", retrievedHistories.get(0).getReport());
        assertEquals(test.getId(), retrievedHistories.get(0).getTest().getId());
        assertEquals(user.getId(), retrievedHistories.get(0).getUser().getId());
    }

}