package org.generalka.storage;

import java.util.Optional;

public interface UserDao {
    void insertUser(User user);
    Optional<User> getUserByUsername(String username);
}