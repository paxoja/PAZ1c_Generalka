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
                TestQuestion testQuestion = new MysqlTestQuestionDao(jdbcTemplate).getTestQuestionById(questionId);

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
    public void updateAnswer(Answer answer) {
        String sql = "UPDATE Answer SET answer=?, is_correct=?, TestQuestion_id=? WHERE id=?";
        jdbcTemplate.update(sql, answer.getAnswer(), answer.getIsCorrect(), answer.getTestQuestion().getId(), answer.getId());
    }

    @Override
    public void deleteAnswer(int answerId) {
        String sql = "DELETE FROM Answer WHERE id=?";
        jdbcTemplate.update(sql, answerId);
    }

    @Override
    public Answer getAnswerById(int answerId) {
        String sql = "SELECT * FROM Answer WHERE id=?";
        return jdbcTemplate.queryForObject(sql, answerRowMapper(), answerId);
    }

    @Override
    public List<Answer> getAllAnswers() {
        String sql = "SELECT * FROM Answer";
        return jdbcTemplate.query(sql, answerRowMapper());
    }

    @Override
    public List<Answer> getAnswersByQuestionId(int questionId) {
        String sql = "SELECT * FROM Answer WHERE TestQuestion_id = ?";
        return jdbcTemplate.query(sql, answerRowMapper(), questionId);
    }
}
