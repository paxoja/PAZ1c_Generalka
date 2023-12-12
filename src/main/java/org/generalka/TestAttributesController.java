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
import org.generalka.storage.DaoFactory;
import org.generalka.storage.Subject;
import org.generalka.storage.SubjectDao;
import org.generalka.storage.Test;
import org.generalka.storage.TestDao;

import java.io.IOException;
import java.util.List;


public class TestAttributesController {

        @FXML
        private TextField descriptionTextField;

        @FXML
        private Button moveToCreateTestButton;

        @FXML
        private Button returnToGeneralkaButton;

        @FXML
        private ComboBox<Subject> semesterComboBox;

        @FXML
        private ComboBox<Subject> subjectComboBox;

        @FXML
        private CheckBox wholeSemesterCheckBox;

        @FXML
        private ComboBox<Integer> yearComboBox;

        private SubjectDao subjectDao = DaoFactory.INSTANCE.getSubjectDao();
        private TestDao testDao = DaoFactory.INSTANCE.getTestDao();

        @FXML
        private void initialize() {
                // Populate ComboBoxes with data from the database
                List<Subject> subjects = subjectDao.getAllSubjects();
                subjectComboBox.getItems().addAll(subjects);
                semesterComboBox.getItems().addAll(subjects);
                yearComboBox.getItems().addAll(/* Fetch years from database */);
        }

        @FXML
        void moveToCreateTest(ActionEvent event) throws IOException {
                // Save test attributes to the database
                Subject selectedSubject = subjectComboBox.getValue();

                Test test = new Test();
                test.setTopic(descriptionTextField.getText());
                test.setIsWholeSemester(wholeSemesterCheckBox.isSelected());
                test.setSubject(selectedSubject);
                // Set yearOfStudy directly from the selected Subject
                test.setYearOfStudy(selectedSubject.getYearOfStudy());

                testDao.saveTest(test);

                // Move to the next scene
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




