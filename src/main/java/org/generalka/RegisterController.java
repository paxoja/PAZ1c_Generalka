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
    private Button button;

    @FXML
    private Button registerButton;

    @FXML
    private PasswordField password;

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
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.setScene(registerScene);
        stage.setTitle("Attender");
    }


    @FXML
    void userRegister(ActionEvent event) {
        User newUser = new User();
        newUser.setAdmin(false); // ak je admoin tak true
        newUser.setUsername(username.getText());
        newUser.setPassword(password.getText());

        userDAO.insertUser(newUser);
    }

}
