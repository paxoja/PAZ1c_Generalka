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

import java.io.IOException;

public class RegisterController {

    private UserDAO userDAO = DaoFactory.getUserDAO();

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
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Attender");
        stage.showAndWait();
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
