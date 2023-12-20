package org.generalka.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

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
        Objects.requireNonNull(testQuestion, "TestQuestion cannot be null");
        Objects.requireNonNull(testQuestion.getQuestion(), "Question text cannot be null");
        Objects.requireNonNull(testQuestion.getTest(), "Test cannot be null");
        Objects.requireNonNull(testQuestion.getTest().getId(), "Test ID cannot be null");

        if (testQuestion.getId() == null) { // INSERT
            String sql = "INSERT INTO TestQuestion (question, Test_id) VALUES (?, ?)";

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, testQuestion.getQuestion());
                statement.setLong(2, testQuestion.getTest().getId());
                return statement;
            }, keyHolder);
            long id = keyHolder.getKey().longValue();
            testQuestion.setId(id);
        } else {    // UPDATE
            String sql = "UPDATE TestQuestion SET question=?, Test_id=? WHERE id=?";

            jdbcTemplate.update(
                    sql,
                    testQuestion.getQuestion(),
                    testQuestion.getTest().getId(),
                    testQuestion.getId()
            );
        }
    }


    @Override
    public TestQuestion getTestQuestionById(Long testQuestionId) {
        String sql = "SELECT * FROM TestQuestion WHERE id=?";
        return jdbcTemplate.queryForObject(sql, testQuestionRowMapper(), testQuestionId);
    }


    @Override
    public List<TestQuestion> getTestQuestionsByTestId(Long testId) {
        String sql = "SELECT * FROM TestQuestion WHERE Test_id = ?";
        return jdbcTemplate.query(sql, testQuestionRowMapper(), testId);
    }
}
