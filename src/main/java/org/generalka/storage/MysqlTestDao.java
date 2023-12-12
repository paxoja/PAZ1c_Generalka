package org.generalka.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MysqlTestDao implements TestDao {
    private JdbcTemplate jdbcTemplate;

    public MysqlTestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Test> testRowMapper() {
        return new RowMapper<Test>() {
            @Override
            public Test mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String topic = rs.getString("topic");
                boolean isWholeSemester = rs.getBoolean("is_whole_semester");
                Date date = rs.getTimestamp("date");
                String subject = rs.getString("subject");
                int semester = rs.getInt("semester");
                int yearOfStudy = rs.getInt("year_of_study");
                int userId = rs.getInt("user_id");

                User user = new User();
                user.setId(userId);

                return new Test(id, name, topic, isWholeSemester, date, subject, semester, yearOfStudy, user);
            }
        };
    }

    @Override
    public void saveTest(Test test) {
        String sql = "INSERT INTO Test (name, topic, is_whole_semester, date, subject, semester, year_of_study, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                test.getName(),
                test.getTopic(),
                test.getIsWholeSemester(),
                test.getDate(),
                test.getSubject(),
                test.getSemester(),
                test.getYearOfStudy(),
                test.getUser().getId()
        );
    }

    @Override
    public void updateTest(Test test) {
        String sql = "UPDATE Test SET name=?, topic=?, is_whole_semester=?, date=?, subject=?, semester=?, year_of_study=?, user_id=? WHERE id=?";
        jdbcTemplate.update(
                sql,
                test.getName(),
                test.getTopic(),
                test.getIsWholeSemester(),
                test.getDate(),
                test.getSubject(),
                test.getSemester(),
                test.getYearOfStudy(),
                test.getUser().getId(),
                test.getId()
        );
    }

    @Override
    public void deleteTest(int testId) {
        String sql = "DELETE FROM Test WHERE id=?";
        jdbcTemplate.update(sql, testId);
    }

    @Override
    public Test getTestById(int testId) {
        String sql = "SELECT * FROM Test WHERE id=?";
        return jdbcTemplate.queryForObject(sql, testRowMapper(), testId);
    }

    @Override
    public List<Test> getAllTests() {
        String sql = "SELECT * FROM Test";
        return jdbcTemplate.query(sql, testRowMapper());
    }
}
