package org.generalka.storage;

import java.util.Optional;

public interface UserDAO {
    void insertUser(User user);
    Optional<User> getUserByUsername(String username);
}
