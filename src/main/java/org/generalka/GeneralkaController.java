package org.generalka;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.generalka.storage.DaoFactory;
import org.generalka.storage.User;
import org.generalka.storage.UserDao;

import java.io.IOException;
import java.util.Optional;

public class GeneralkaController {

    @FXML
    private Button logoutButton;

    @FXML
    private Button moveToCreateTestButton;

    @FXML
    private Button moveToProfileButton;

    @FXML
    private Button moveToTakeTestButton;

    @FXML
    private Label loggedInUserLabel; // na menenie mena v generalke

    // we call the user instance using DaoFactory
    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();

    // after loading the FXML we retrieve current user from DAO
    // if  we have a user, it sets its username to the top of the screen
    // we use Optional, which finds the presence of the object through get
    @FXML
    private void initialize() {
        Optional<User> currentUser = userDao.getCurrentUser();
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            loggedInUserLabel.setText(user.getUsername() + "!");
        }
    }


    // move to profile using button
    @FXML
    private void moveToProfile() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Profile.fxml"));
        Parent parent = loader.load();
        Scene profileScene = new Scene(parent);
        profileScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) moveToProfileButton.getScene().getWindow();
        stage.setScene(profileScene);


    }

    // move to create test using button
    @FXML
    private void moveToCreateTest() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/TestAttributes.fxml"));
        Parent parent = loader.load();
        Scene createTestScene = new Scene(parent);
        createTestScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) moveToCreateTestButton.getScene().getWindow();
        stage.setScene(createTestScene);

    }

    // move to take test using button
    @FXML
    private void moveToTakeTest() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/TestSelection.fxml"));
        Parent parent = loader.load();
        Scene takeTestScene = new Scene(parent);
        takeTestScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) moveToTakeTestButton.getScene().getWindow();
        stage.setScene(takeTestScene);

    }

    // logout using button
    @FXML
    private void logout() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/login.fxml"));
        Parent parent = loader.load();
        Scene logoutScene = new Scene(parent);
        logoutScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.setScene(logoutScene);

    }
}
