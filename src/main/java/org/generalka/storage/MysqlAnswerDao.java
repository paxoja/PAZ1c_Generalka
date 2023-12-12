package org.generalka.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MysqlAnswerDao implements AnswerDao {
    private JdbcTemplate jdbcTemplate;

    public MysqlAnswerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Answer> answerRowMapper() {
        return new RowMapper<Answer>() {
            @Override
            public Answer mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("id");
                String answer = rs.getString("answer");
                boolean isCorrect = rs.getBoolean("is_correct");
                int questionId = rs.getInt("TestQuestion_id");

                // You may need to fetch the actual test question from the database
                TestQuestion testQuestion = new TestQuestion(questionId, null, null, null);

                return new Answer(id, answer, isCorrect, testQuestion);
            }
        };
    }

    @Override
    public void saveAnswer(Answer answer) {
        String sql = "INSERT INTO Answer (answer, is_correct, TestQuestion_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, answer.getAnswer(), answer.getIsCorrect(), answer.getTestQuestion().getId());
    }

    @Override
    public List<Answer> getAnswersByQuestionId(int questionId) {
        String sql = "SELECT * FROM Answer WHERE TestQuestion_id = ?";
        return jdbcTemplate.query(sql, answerRowMapper(), questionId);
    }
}
