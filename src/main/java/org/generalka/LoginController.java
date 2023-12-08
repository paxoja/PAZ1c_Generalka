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
    private Button registerButton;
    @FXML
    private Label wrongLogIn;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();

    public void moveToRegister(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/register.fxml"));
        Parent parent = loader.load();
        Scene registerScene = new Scene(parent);
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(registerScene);
        stage.setTitle("Attender");
    }

    public void userLogin(ActionEvent event) throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            wrongLogIn.setText("Please enter your username and password.");
            return;
        }

        Optional<User> userOptional = userDao.getUserByUsername(enteredUsername);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (enteredPassword.equals(user.getPassword())) {
                wrongLogIn.setText("Login successful!");
                openGeneralkaScreen();
            } else {
                wrongLogIn.setText("Wrong password!");
            }
        } else {
            wrongLogIn.setText("User not found.");
        }
    }

    private void openGeneralkaScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/generalka.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
