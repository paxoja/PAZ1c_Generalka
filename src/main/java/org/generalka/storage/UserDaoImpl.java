package org.generalka.storage;
import java.util.Optional;
public class UserDaoImpl implements UserDao {
    private User currentUser; // tu sa ulozi logged in user

    @Override
    public void insertUser(User user) {

    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
