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

public class MysqlAnswerDao implements AnswerDao {
    private JdbcTemplate jdbcTemplate;

    public MysqlAnswerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Answer> answerRowMapper() {
        return new RowMapper<Answer>() {
            @Override
            public Answer mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String answer = rs.getString("answer");
                boolean isCorrect = rs.getBoolean("is_correct");
                Long questionId = rs.getLong("TestQuestion_id");

                TestQuestion testQuestion = new MysqlTestQuestionDao(jdbcTemplate).getTestQuestionById(questionId);

                return new Answer(id, answer, isCorrect, testQuestion);
            }
        };
    }

    @Override
    public void saveAnswer(Answer answer) {

        try {
        Objects.requireNonNull(answer, "Answer cannot be null");
        Objects.requireNonNull(answer.getAnswer(), "Answer text cannot be null");
        Objects.requireNonNull(answer.getTestQuestion(), "TestQuestion cannot be null");
        Objects.requireNonNull(answer.getTestQuestion().getId(), "TestQuestion ID cannot be null");

        if (answer.getId() == null) { // INSERT
            String sql = "INSERT INTO Answer (answer, is_correct, TestQuestion_id) VALUES (?, ?, ?)";

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, answer.getAnswer());
                statement.setBoolean(2, answer.getIsCorrect());
                statement.setLong(3, answer.getTestQuestion().getId());
                return statement;
            }, keyHolder);
            long id = keyHolder.getKey().longValue();
            answer.setId(id);
        } else {    // UPDATE
            String sql = "UPDATE Answer SET answer=?, is_correct=?, TestQuestion_id=? WHERE id=?";

            jdbcTemplate.update(
                    sql,
                    answer.getAnswer(),
                    answer.getIsCorrect(),
                    answer.getTestQuestion().getId(),
                    answer.getId()
            );
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteAnswer(Long answerId) {
        String sql = "DELETE FROM Answer WHERE id=?";
        jdbcTemplate.update(sql, answerId);
    }

    @Override
    public Answer getAnswerById(Long answerId) {
        String sql = "SELECT * FROM Answer WHERE id=?";
        return jdbcTemplate.queryForObject(sql, answerRowMapper(), answerId);
    }

    @Override
    public List<Answer> getAllAnswers() {
        String sql = "SELECT * FROM Answer";
        return jdbcTemplate.query(sql, answerRowMapper());
    }

    @Override
    public List<Answer> getAnswersByQuestionId(Long questionId) {
        String sql = "SELECT * FROM Answer WHERE TestQuestion_id = ?";
        return jdbcTemplate.query(sql, answerRowMapper(), questionId);
    }
}
