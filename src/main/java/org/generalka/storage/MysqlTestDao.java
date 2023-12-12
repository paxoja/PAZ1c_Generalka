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
                String topic = rs.getString("topic");
                boolean isWholeSemester = rs.getBoolean("is_whole_semester");
                Date date = rs.getTimestamp("date");
                int subjectId = rs.getInt("Subject_id");
                int userId = rs.getInt("User_id");

                Subject subject = new Subject(subjectId, null, 0, 0); // You may need to fetch the actual subject from the database
                User user = new User();
                user.setId(userId);

                return new Test(id, topic, isWholeSemester, date, subject, user, null);
            }
        };
    }

    @Override
    public void saveTest(Test test) {
        String sql = "INSERT INTO Test (name, topic, is_whole_semester, date, Subject_id, User_id) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, test.getTopic(), test.getIsWholeSemester(), test.getDate(), test.getSubject().getId(), test.getUser().getId());
    }

    @Override
    public List<Test> getAllTests() {
        String sql = "SELECT * FROM Test";
        return jdbcTemplate.query(sql, testRowMapper());
    }
}

