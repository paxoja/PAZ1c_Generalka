package org.generalka;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class TestAttributesController {

        @FXML
        private TextField descriptionTextField;

        @FXML
        private Button moveToCreateTestButton;

        @FXML
        private Button returnToGeneralkaButton;

        @FXML
        private ComboBox<?> semesterComboBox;

        @FXML
        private ComboBox<?> subjectComboBox;

        @FXML
        private CheckBox wholeSemesterCheckBox;

        @FXML
        private ComboBox<?> yearComboBox;

        @FXML
        void moveToCreateTest(ActionEvent event) throws IOException {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/TestCreator.fxml"));
                Parent parent = loader.load();
                Scene createTestScene = new Scene(parent);
                Stage stage = (Stage) moveToCreateTestButton.getScene().getWindow();
                stage.setScene(createTestScene);
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





