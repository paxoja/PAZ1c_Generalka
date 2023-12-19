package org.generalka.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.*;
import java.util.List;
import java.util.Objects;

public class MysqlTestHistoryDao implements TestHistoryDao {
    private JdbcTemplate jdbcTemplate;

    private TestDao testDao = DaoFactory.INSTANCE.getTestDao();

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
                Timestamp date = rs.getTimestamp("date");
                Long testId = rs.getLong("Test_id");
                int userId = rs.getInt("User_id");

                Test test = testDao.getTestById(testId);
                User user = new User();
                user.setId(userId);

                return new TestHistory(id, score, report, date, test, user);
            }
        };
    }


    @Override
    public TestHistory getTestHistoryById(Long id) throws EntityNotFoundException {
        String sql = "SELECT * FROM TestHistory WHERE id=?";
        return jdbcTemplate.queryForObject(sql, testHistoryRowMapper(), id);
    }

    @Override
    public List<TestHistory> getAllTestHistories() throws EntityNotFoundException {
        String sql = "SELECT * FROM TestHistory";
        return jdbcTemplate.query(sql, testHistoryRowMapper());
    }

    @Override
    public void deleteTestHistory(Long id) throws EntityNotFoundException {
        String sql = "DELETE FROM TestHistory WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void saveTestHistory(TestHistory testHistory) {
        Objects.requireNonNull(testHistory, "TestHistory cannot be null");
        Objects.requireNonNull(testHistory.getTest(), "Test cannot be null");
        Objects.requireNonNull(testHistory.getTest().getId(), "Test ID cannot be null");

        if (testHistory.getId() == null) { // INSERT
            String sql = "INSERT INTO TestHistory (score, report, date, Test_id, User_id) VALUES (?, ?, ?, ?, ?)";

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, testHistory.getScore());
                statement.setString(2, testHistory.getReport());
                statement.setTimestamp(3, testHistory.getDate() != null ? testHistory.getDate() : new Timestamp(System.currentTimeMillis()));
                statement.setLong(4, testHistory.getTest().getId());
                statement.setLong(5, testHistory.getUser().getId());
                return statement;
            }, keyHolder);
            long id = keyHolder.getKey().longValue();
            testHistory.setId(id);
        } else {    // UPDATE
            String sql = "UPDATE TestHistory SET score=?, report=?, date=?, Test_id=?, User_id=? WHERE id=?";

            jdbcTemplate.update(
                    sql,
                    testHistory.getScore(),
                    testHistory.getReport(),
                    testHistory.getDate(),
                    testHistory.getTest().getId(),
                    testHistory.getUser().getId(),
                    testHistory.getId()
            );
        }
    }

    @Override
    public List<TestHistory> getTestHistoryByUserId(Long userId) throws EntityNotFoundException {
        String sql = "SELECT * FROM TestHistory WHERE User_id=?";
        return jdbcTemplate.query(sql, testHistoryRowMapper(), userId);

    }
}
