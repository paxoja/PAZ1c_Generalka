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

    // z DaoFactory volame pomocou instance userDao
    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();

    // po loadnuti FXML ziskame current user z Dao, ak mame pouzivatela tak nastavi jeho username
    // pouzivame Optional, co zisti pritomnost objektu cez get
    @FXML
    private void initialize() {
        Optional<User> currentUser = userDao.getCurrentUser();
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            loggedInUserLabel.setText(user.getUsername() + "!");
        }
    }

    @FXML
    private void moveToProfile() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Profile.fxml"));
        Parent parent = loader.load();
        Scene profileScene = new Scene(parent);
        Stage stage = (Stage) moveToProfileButton.getScene().getWindow();
        stage.setScene(profileScene);

    }

    @FXML
    private void moveToCreateTest() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/TestAttributes.fxml"));
        Parent parent = loader.load();
        Scene createTestScene = new Scene(parent);
        Stage stage = (Stage) moveToCreateTestButton.getScene().getWindow();
        stage.setScene(createTestScene);

    }

    @FXML
    private void moveToTakeTest() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/TestSelection.fxml"));
        Parent parent = loader.load();
        Scene takeTestScene = new Scene(parent);
        Stage stage = (Stage) moveToTakeTestButton.getScene().getWindow();
        stage.setScene(takeTestScene);

    }

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
