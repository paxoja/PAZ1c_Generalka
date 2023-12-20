package org.generalka.storage;
import java.util.Optional;
public class UserDaoImpl implements UserDao {
    private User currentUser; // holds the currently logged-in user

    @Override
    public void insertUser(User user) { }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.empty();
    }

    // returns an Optional containing the currently logged in user, may be null
    @Override
    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    // sets the currentUser field with the provided user
    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
