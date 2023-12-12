package org.generalka.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;

public class MysqlUserDao implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public MysqlUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertUser(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        Objects.requireNonNull(user.getPassword(), "Password cannot be null");

        String query = "INSERT INTO User (is_admin, username, password) VALUES (?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setBoolean(1, user.isAdmin());
                statement.setString(2, user.getUsername());
                statement.setString(3, user.getPassword());
                return statement;
            }
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        user.setId(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        String query = "SELECT * FROM User WHERE username = ?";
        try {
            User user = jdbcTemplate.queryForObject(query, new Object[]{username},
                    new BeanPropertyRowMapper<>(User.class));
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getCurrentUser() {
        return Optional.empty();
    }
}