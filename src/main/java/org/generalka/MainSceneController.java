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

import javax.security.auth.Subject;
import java.io.IOException;
public class MainSceneController {

    public MainSceneController() {

    }

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



    public void moveToRegister (ActionEvent event) throws  IOException{
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/register.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Attender");
        stage.showAndWait();
    }


    public void userLogin(ActionEvent event) throws IOException {
        checkLogin();

    }

    private void checkLogin() throws IOException {
        Generalka m = new Generalka();
        if(username.getText().toString().equals("javacoding") && password.getText().toString().equals("123")) {
            wrongLogIn.setText("Success!");

        }

        else if(username.getText().isEmpty() && password.getText().isEmpty()) {
            wrongLogIn.setText("Please enter your data.");
        }


        else {
            wrongLogIn.setText("Wrong username or password!");
        }
    }


}