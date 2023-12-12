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
import org.generalka.storage.*;

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

        private TestFxModel testFxModel;

        private TestDao testDao = DaoFactory.INSTANCE.getTestDao();

        public TestAttributesController(){testFxModel = new TestFxModel();}

        @FXML
        private void initialize() {
                descriptionTextField.textProperty().bindBidirectional(testFxModel.topicProperty());
                wholeSemesterCheckBox.selectedProperty().bindBidirectional(testFxModel.isWholeSemesterProperty());

                // Populate ComboBoxes with data
                ObservableList<Integer> years = FXCollections.observableArrayList(1, 2, 3);
                yearComboBox.setItems(years);

                ObservableList<String> subjects = FXCollections.observableArrayList("UGR1", "NUM", "MZI", "UGR1", "SXM1", "PAZ1c", "OSY1", "DGS", "BDS1a", "MSU", "DSM3a");
                subjectComboBox.getItems().addAll(subjects);

                ObservableList<String> semesters = FXCollections.observableArrayList("winter", "summer");
                semesterComboBox.setItems(semesters);
        }

        @FXML
        void moveToCreateTest(ActionEvent event) throws IOException {
                // Create a Test object with the entered attributes
                Test test = new Test();
                test.setTopic(descriptionTextField.getText());
                test.setYearOfStudy(yearComboBox.getValue());
                test.setSubject(subjectComboBox.getValue());
                test.setSemester(semesterComboBox.getValue());
                test.setIsWholeSemester(wholeSemesterCheckBox.isSelected());


                try {
                        // Save the test to the database
                        testDao.saveTest(test);

                        // Move to the create test scene
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TestCreator.fxml"));
                        Parent parent = loader.load();
                        Scene createTestScene = new Scene(parent);
                        Stage stage = (Stage) moveToCreateTestButton.getScene().getWindow();
                        stage.setScene(createTestScene);
                } catch (EntityNotFoundException e) {

                        e.printStackTrace();
                }
        }

        @FXML
        private void returnToGeneralka() throws IOException {
                // Move back to the Generalka scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/generalka.fxml"));
                Parent parent = loader.load();
                Scene generalkaScene = new Scene(parent);
                Stage stage = (Stage) returnToGeneralkaButton.getScene().getWindow();
                stage.setScene(generalkaScene);
        }
}
