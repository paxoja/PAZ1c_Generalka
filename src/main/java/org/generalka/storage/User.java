package org.generalka.storage;

public class User {

    // vytvorenie premennych podla tabulky User
    private long id;
    private boolean isAdmin;
    private String username;
    private String password;

    // getters + setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // doplnit age/datum narodenia a country dropbox aby to nebolo take prazdne v profile

}
