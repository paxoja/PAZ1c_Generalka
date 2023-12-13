package org.generalka;

import javafx.event.ActionEvent;
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

public class ProfileController {

    @FXML
    private Label profileUsername;

    @FXML
    private Label ageLabel;

    @FXML
    private Label countryLabel;

    @FXML
    private Label genderLabel;

    @FXML
    private Button returnToGeneralkaButton;

    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();

    // po loadnuti FXML ziskame current user z Dao, ak mame pouzivatela tak nastavi jeho username
    // pouzivame Optional, co zisti pritomnost objektu cez get
    @FXML
    private void initialize() {
        Optional<User> currentUser = userDao.getCurrentUser();
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            profileUsername.setText(user.getUsername());
        }
    }

    @FXML
    private void returnToGeneralka() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/generalka.fxml"));
        Parent parent = loader.load();
        Scene generalkaScene = new Scene(parent);
        Stage stage = (Stage) returnToGeneralkaButton.getScene().getWindow();
        stage.setScene(generalkaScene);
    }

}