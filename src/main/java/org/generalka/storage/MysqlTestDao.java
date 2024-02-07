package org.generalka.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.*;
import java.util.List;
import java.util.Objects;

public class MysqlTestDao implements TestDao {
    private JdbcTemplate jdbcTemplate;

    public MysqlTestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // fetching the tests from the database
    private RowMapper<Test> testRowMapper() {
        return new RowMapper<Test>() {
            @Override
            public Test mapRow(ResultSet rs, int rowNum) throws SQLException {
                long id = rs.getLong("id");
                String topic = rs.getString("topic");
                boolean isWholeSemester = rs.getBoolean("is_whole_semester");
                Timestamp date = rs.getTimestamp("date");
                String subject = rs.getString("subject");
                String semester = rs.getString("semester");
                int yearOfStudy = rs.getInt("year_of_study");
                int userId = rs.getInt("user_id");

                User user = null;
                if (!rs.wasNull()) {
                    user = new User();
                    user.setId(userId);
                }
                return new Test(id, topic, isWholeSemester, date, subject, semester, yearOfStudy, user);
            }
        };
    }

    @Override
    public void saveTest(Test test) {
        Objects.requireNonNull(test, "Test cannot be null");
        Objects.requireNonNull(test.getTopic(), "Test topic cannot be null");
        Objects.requireNonNull(test.getSubject(), "Test subject cannot be null");
        Objects.requireNonNull(test.getSemester(), "Test semester cannot be null");
        Objects.requireNonNull(test.getYearOfStudy(), "Test year of study cannot be null");
        Objects.requireNonNull(test.getUser(), "Test user cannot be null");
        Objects.requireNonNull(test.getUser().getId(), "User ID cannot be null");

        if (test.getId() == null) {
            String sql = "INSERT INTO Test (topic, is_whole_semester, date, subject, semester, year_of_study, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";


            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, test.getTopic());
                statement.setBoolean(2, test.getIsWholeSemester());
                statement.setTimestamp(3, test.getDate());
                statement.setString(4, test.getSubject());
                statement.setString(5, test.getSemester());
                statement.setInt(6, test.getYearOfStudy());
                statement.setLong(7, test.getUser().getId());
                return statement;
            }, keyHolder);
            long id = keyHolder.getKey().longValue();
            test.setId(id);
        } else {
            String sql = "UPDATE Test SET topic=?, is_whole_semester=?, date=?, subject=?, semester=?, year_of_study=?, user_id=? WHERE id=?";


            jdbcTemplate.update(
                    sql,
                    test.getTopic(),
                    test.getIsWholeSemester(),
                    new Timestamp(test.getDate().getTime()),
                    test.getSubject(),
                    test.getSemester(),
                    test.getYearOfStudy(),
                    test.getUser().getId(),
                    test.getId()
            );
        }
    }


    // deleting specific test based on id
    @Override
    public void deleteTest(Long testId) {
        String sql = "DELETE FROM Test WHERE id=?";
        jdbcTemplate.update(sql, testId);
    }

    // fetching test
    @Override
    public Test getTestById(Long testId) {
        String sql = "SELECT * FROM Test WHERE id=?";
        return jdbcTemplate.queryForObject(sql, testRowMapper(), testId);
    }

    // fetching all tests to show in table
    @Override
    public List<Test> getAllTests() {
        String sql = "SELECT * FROM Test";
        return jdbcTemplate.query(sql, testRowMapper());
    }

    // getting number of questions - to count possible points
    @Override
    public int getNumberOfQuestions(Long testId) throws EntityNotFoundException {
        String sql = "SELECT COUNT(*) FROM TestQuestion WHERE Test_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, testId);
    }

    @Override
    public void updateTest(Test test) {
        Objects.requireNonNull(test, "Test cannot be null");
        Objects.requireNonNull(test.getId(), "Test ID cannot be null");
        Objects.requireNonNull(test.getTopic(), "Test topic cannot be null");
        Objects.requireNonNull(test.getSubject(), "Test subject cannot be null");
        Objects.requireNonNull(test.getSemester(), "Test semester cannot be null");
        Objects.requireNonNull(test.getYearOfStudy(), "Test year of study cannot be null");
        Objects.requireNonNull(test.getUser(), "Test user cannot be null");
        Objects.requireNonNull(test.getUser().getId(), "User ID cannot be null");

        String sql = "UPDATE Test SET topic=?, is_whole_semester=?, date=?, subject=?, semester=?, year_of_study=?, user_id=? WHERE id=?";

        jdbcTemplate.update(
                sql,
                test.getTopic(),
                test.getIsWholeSemester(),
                new Timestamp(test.getDate().getTime()),
                test.getSubject(),
                test.getSemester(),
                test.getYearOfStudy(),
                test.getUser().getId(),
                test.getId()
        );
    }

    @Override
    public void updateTestAttribute(Long testId, String attributeName, Object attributeValue) throws EntityNotFoundException {
        String sql = "UPDATE Test SET " + attributeName + "=? WHERE id=?";
        jdbcTemplate.update(sql, attributeValue, testId);
    }

    @Override
    public void updateTestEditAttribute(Long testId, String attributeName, Object attributeValue) throws EntityNotFoundException {
        String sql = "UPDATE Test SET " + attributeName + "=? WHERE id=?";
        jdbcTemplate.update(sql, attributeValue, testId);
    }

}
