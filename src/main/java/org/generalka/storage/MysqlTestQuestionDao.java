package org.generalka.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MysqlTestQuestionDao implements TestQuestionDao {
    private JdbcTemplate jdbcTemplate;

    public MysqlTestQuestionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<TestQuestion> testQuestionRowMapper() {
        return new RowMapper<TestQuestion>() {
            @Override
            public TestQuestion mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String question = rs.getString("question");
                Long testId = rs.getLong("Test_id");

                Test test = new MysqlTestDao(jdbcTemplate).getTestById(testId);

                return new TestQuestion(id, question, test, null);
            }
        };
    }

    @Override
    public void saveTestQuestion(TestQuestion testQuestion) {
        String sql = "INSERT INTO TestQuestion (question, Test_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, testQuestion.getQuestion(), testQuestion.getTest().getId());
    }

    @Override
    public void updateTestQuestion(TestQuestion testQuestion) {
        String sql = "UPDATE TestQuestion SET question=?, Test_id=? WHERE id=?";
        jdbcTemplate.update(sql, testQuestion.getQuestion(), testQuestion.getTest().getId(), testQuestion.getId());
    }

    @Override
    public void deleteTestQuestion(Long testQuestionId) {
        String sql = "DELETE FROM TestQuestion WHERE id=?";
        jdbcTemplate.update(sql, testQuestionId);
    }

    @Override
    public TestQuestion getTestQuestionById(Long testQuestionId) {
        String sql = "SELECT * FROM TestQuestion WHERE id=?";
        return jdbcTemplate.queryForObject(sql, testQuestionRowMapper(), testQuestionId);
    }

    @Override
    public List<TestQuestion> getAllTestQuestions() {
        String sql = "SELECT * FROM TestQuestion";
        return jdbcTemplate.query(sql, testQuestionRowMapper());
    }

    @Override
    public List<TestQuestion> getTestQuestionsByTestId(Long testId) {
        String sql = "SELECT * FROM TestQuestion WHERE Test_id = ?";
        return jdbcTemplate.query(sql, testQuestionRowMapper(), testId);
    }
}
