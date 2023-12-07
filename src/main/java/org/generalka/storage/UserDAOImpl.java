package org.generalka.storage;
import org.generalka.storage.DaoFactory;
import org.generalka.storage.User;
import org.generalka.storage.UserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private static final String INSERT_USER_SQL = "INSERT INTO User (is_admin, username, password) VALUES (?, ?, ?)";
    private static final String SELECT_USER_BY_USERNAME_SQL = "SELECT * FROM User WHERE username = ?";

    @Override
    public void insertUser(User user) {
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setBoolean(1, user.isAdmin());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately (log or throw a custom exception)
        }
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERNAME_SQL)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setAdmin(resultSet.getBoolean("is_admin"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately (log or throw a custom exception)
        }
        return Optional.empty();
    }
}
