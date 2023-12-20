package org.generalka.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;

public enum DaoFactory {

    INSTANCE;

    private UserDao userDao;
    private TestDao testDao;
    private TestQuestionDao testQuestionDao;
    private AnswerDao answerDao;
    private TestHistoryDao testHistoryDao;
    private JdbcTemplate jdbcTemplate;


    // setting up the database
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

    // inicialization of the database for unit tests
    private DataSource getDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
        return dataSource;
    }

    // getters of instances for Daos
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

    public TestHistoryDao getTestHistoryDao(){
        if (testHistoryDao == null)
            testHistoryDao = new MysqlTestHistoryDao(getJdbcTemplate());
            return testHistoryDao;
    }

    // calling the method
    public DataSource getFullDataSource() {
        return getDataSource();
    }

}
