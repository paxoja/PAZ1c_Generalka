package org.generalka;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.generalka.storage.DaoFactory;
import org.generalka.storage.User;
import org.generalka.storage.UserDao;

import java.io.IOException;

public class RegisterController {

    private UserDao userDAO = DaoFactory.INSTANCE.getUserDao();


    @FXML
    private Button moveToLoginButton;

    @FXML
    private PasswordField password;

    @FXML
    private Button registerButton;

    @FXML
    private TextField username;

    @FXML
    private Label wrongUsername;


    @FXML
    void moveToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/login.fxml"));
        Parent parent = loader.load();
        Scene registerScene = new Scene(parent);
        registerScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) moveToLoginButton.getScene().getWindow();
        stage.setScene(registerScene);
        stage.setTitle("Attender");
    }


    @FXML
    void userRegister() throws IOException{
        User newUser = new User();
        newUser.setUsername(username.getText());
        newUser.setPassword(password.getText());
        userDAO.insertUser(newUser);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/login.fxml"));
        Parent parent = loader.load();
        Scene registerScene = new Scene(parent);
        registerScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) moveToLoginButton.getScene().getWindow();
        stage.setScene(registerScene);
        stage.setTitle("Attender");
    }

}
