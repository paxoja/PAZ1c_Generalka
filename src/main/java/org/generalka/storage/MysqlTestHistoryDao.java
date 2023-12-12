package org.generalka.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MysqlTestHistoryDao implements TestHistoryDao {
    private JdbcTemplate jdbcTemplate;

    public MysqlTestHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<TestHistory> testHistoryRowMapper() {
        return new RowMapper<TestHistory>() {
            @Override
            public TestHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                int score = rs.getInt("score");
                String report = rs.getString("report");
                java.util.Date date = rs.getDate("date");
                Long testId = rs.getLong("Test_id");
                int userId = rs.getInt("User_id");

                Test test = new MysqlTestDao(jdbcTemplate).getTestById(testId);
                User user = new User();
                user.setId(userId);

                return new TestHistory(id, score, report, date, test, user);
            }
        };
    }

    @Override
    public void saveTestHistory(TestHistory testHistory) {
        String sql = "INSERT INTO TestHistory (score, report, date, Test_id, User_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, testHistory.getScore(), testHistory.getReport(), testHistory.getDate(),
                testHistory.getTest().getId(), testHistory.getUser().getId());
    }

    @Override
    public void addTestHistory(TestHistory testHistory) throws EntityNotFoundException {

    }

    @Override
    public TestHistory getTestHistoryById(Long id) {
        String sql = "SELECT * FROM TestHistory WHERE id=?";
        return jdbcTemplate.queryForObject(sql, testHistoryRowMapper(), id);
    }

    @Override
    public List<TestHistory> getAllTestHistories() {
        String sql = "SELECT * FROM TestHistory";
        return jdbcTemplate.query(sql, testHistoryRowMapper());
    }

    @Override
    public void updateTestHistory(TestHistory testHistory) {
        String sql = "UPDATE TestHistory SET score=?, report=?, date=?, Test_id=?, User_id=? WHERE id=?";
        jdbcTemplate.update(sql, testHistory.getScore(), testHistory.getReport(), testHistory.getDate(),
                testHistory.getTest().getId(), testHistory.getUser().getId(), testHistory.getId());
    }

    @Override
    public void deleteTestHistory(Long id) {
        String sql = "DELETE FROM TestHistory WHERE id=?";
        jdbcTemplate.update(sql, id);
    }
}
