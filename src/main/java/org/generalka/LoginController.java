package org.generalka;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.generalka.storage.DaoFactory;
import org.generalka.storage.User;
import org.generalka.storage.UserDao;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private Button moveToRegisterButton;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Label wrongLogIn;

    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();

    // move to register using button
    public void moveToRegister() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/register.fxml"));
        Parent parent = loader.load();
        Scene registerScene = new Scene(parent);
        registerScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) moveToRegisterButton.getScene().getWindow();
        stage.setScene(registerScene);
        stage.setTitle("Generalka");
    }

    public void userLogin() throws IOException {
        checkLogin();
    }


    // source: https://www.youtube.com/watch?v=HBBtlwGpBek
    private void checkLogin() throws IOException {
        // we retrieve entered text
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

        // we need to provide the username and password
        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            wrongLogIn.setText("Please enter your username and password.");
            return;
        }

        // if the password and username matches and the user is also in database, the login is successful
        Optional<User> userOptional = userDao.getUserByUsername(enteredUsername);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (enteredPassword.equals(user.getPassword())) {
                wrongLogIn.setText("Login successful!");
                userDao.setCurrentUser(user);
                openGeneralkaScreen();
            } else {
                // if some credential is incorrect text pops out
                wrongLogIn.setText("Your username or password is incorrect.");
            }
        } else {
            wrongLogIn.setText("Your username or password is incorrect.");
        }
    }

    // move to main screen using login button
    private void openGeneralkaScreen() throws IOException {
        Optional<User> optionalUser = userDao.getCurrentUser();

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Retrieve isAdmin information from the database and update the User object
            boolean isAdmin = userDao.getUserIsAdminFromDatabase(user.getId());
            user.setAdmin(isAdmin);
        }

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/generalka.fxml"));
        Parent parent = loader.load();

        Scene generalkaScene = new Scene(parent);

        generalkaScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(generalkaScene);

    }
}
