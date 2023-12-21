package org.generalka.storage;

import java.util.Optional;

public interface UserDao {
    // creating new user and inserting into database - in registercontroller
    void insertUser(User user);

    // fetching which user we are logging in as
    Optional<User> getUserByUsername(String username);

    // getting logged in user to set as current user - used for tests, to show username in generalkascene
    Optional<User> getCurrentUser();

    // setting logged in user as current user
    void setCurrentUser(User user);

    // set user admin
    boolean getUserIsAdminFromDatabase(long userId);
}

