package org.generalka.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import com.mysql.cj.jdbc.MysqlDataSource;

public enum DaoFactory {

    INSTANCE;

    private UserDao userDao;
    private TestDao testDao;
    private TestQuestionDao testQuestionDao;
    private AnswerDao answerDao;
    private SubjectDao subjectDao;
    private JdbcTemplate jdbcTemplate;

    private JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser("root");
            dataSource.setPassword("root");
            dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        return jdbcTemplate;
    }

    public UserDao getUserDao() {
        if (userDao == null)
            userDao = new MysqlUserDao(getJdbcTemplate());
        return userDao;
    }

    public TestDao getTestDao() {
        if (testDao == null)
            testDao = new MysqlTestDao(getJdbcTemplate());
        return testDao;
    }

    public TestQuestionDao getTestQuestionDao() {
        if (testQuestionDao == null)
            testQuestionDao = new MysqlTestQuestionDao(getJdbcTemplate());
        return testQuestionDao;
    }

    public AnswerDao getAnswerDao() {
        if (answerDao == null)
            answerDao = new MysqlAnswerDao(getJdbcTemplate());
        return answerDao;
    }

    public SubjectDao getSubjectDao() {
        if (subjectDao == null)
            subjectDao = new MysqlSubjectDao(getJdbcTemplate());
        return subjectDao;
    }
}
