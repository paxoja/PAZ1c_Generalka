package org.generalka;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TestAttributesController {

        @FXML
        private Button moveToCreateTestButton;

        @FXML
        private Button returnToGeneralkaButton;

        @FXML
        private TextField descriptionTextField;

        @FXML
        private ComboBox<Integer> yearComboBox;

        @FXML
        private ComboBox<String> subjectComboBox;

        @FXML
        private ComboBox<String> semesterComboBox;

        @FXML
        private CheckBox wholeSemesterCheckBox;

        @FXML
        private void initialize() {
                // Populate ComboBoxes with data
                ObservableList<Integer> years = FXCollections.observableArrayList(1, 2, 3, 4); // Add your actual years
                yearComboBox.setItems(years);

                List<String> subjects = Arrays.asList("Subject A", "Subject B", "Subject C"); // Add your actual subjects
                subjectComboBox.getItems().addAll(subjects);

                ObservableList<String> semesters = FXCollections.observableArrayList("Semester 1", "Semester 2"); // Add your actual semesters
                semesterComboBox.setItems(semesters);
        }

        @FXML
        void moveToCreateTest(ActionEvent event) throws IOException {
                // Save test attributes to the database or perform any necessary logic

                // Move to the create test scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/CreateTest.fxml")); // Replace with the actual path
                Parent parent = loader.load();
                Scene createTestScene = new Scene(parent);
                Stage stage = (Stage) moveToCreateTestButton.getScene().getWindow();
                stage.setScene(createTestScene);
        }

        @FXML
        private void returnToGeneralka() throws IOException {
                // Move back to the Generalka scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/Generalka.fxml")); // Replace with the actual path
                Parent parent = loader.load();
                Scene generalkaScene = new Scene(parent);
                Stage stage = (Stage) returnToGeneralkaButton.getScene().getWindow();
                stage.setScene(generalkaScene);
        }
}
