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
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

        private TestDao testDao = DaoFactory.INSTANCE.getTestDao();

        private TestFxModel testFxModel;

        public TestAttributesController() {
                testFxModel = new TestFxModel();
        }


        // initializes comboboxes
        @FXML
        private void initialize() {
                descriptionTextField.textProperty().bindBidirectional(testFxModel.topicProperty());
                wholeSemesterCheckBox.selectedProperty().bindBidirectional(testFxModel.isWholeSemesterProperty());

                ObservableList<Integer> years = FXCollections.observableArrayList(1, 2, 3);
                yearComboBox.setItems(years);

                ObservableList<String> subjects = FXCollections.observableArrayList("UGR1", "NUM", "MZI", "UGR1", "SXM1", "PAZ1c", "OSY1", "DGS", "BDS1a", "MSU", "DSM3a");
                subjectComboBox.getItems().addAll(subjects);

                ObservableList<String> semesters = FXCollections.observableArrayList("winter", "summer");
                semesterComboBox.setItems(semesters);
        }

        // return button
        @FXML
        void moveToCreateTest() throws IOException {
                Test test = createTest();

                if (test != null) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TestCreator.fxml"));
                        Parent parent = loader.load();
                        TestCreatorController testCreatorController = loader.getController();
                        testCreatorController.setTest(test);
                        Scene createTestScene = new Scene(parent);
                        createTestScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
                        Stage stage = (Stage) moveToCreateTestButton.getScene().getWindow();
                        stage.setScene(createTestScene);
                }
        }


        private Test createTest() {
                // chosen attributes save to test
                Test test = new Test();
                test.setTopic(descriptionTextField.getText());
                test.setYearOfStudy(yearComboBox.getValue());
                test.setSubject(subjectComboBox.getValue());
                test.setSemester(semesterComboBox.getValue());
                test.setIsWholeSemester(wholeSemesterCheckBox.isSelected());
                test.setDate(new Timestamp(System.currentTimeMillis()));

                // we get user ids to define which user created it
                UserDao userDao = DaoFactory.INSTANCE.getUserDao();
                Optional<User> currentUser = userDao.getCurrentUser();

                if (currentUser.isPresent()) {
                        test.setUser(currentUser.get());
                        try {
                                testDao.saveTest(test);
                                return test;
                        } catch (EntityNotFoundException e) {
                                e.printStackTrace();
                        }
                } else {
                        System.err.println("No current user found");
                }

                return null;
        }


        // return
        @FXML
        private void returnToGeneralka() throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/generalka.fxml"));
                Parent parent = loader.load();
                Scene generalkaScene = new Scene(parent);
                generalkaScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
                Stage stage = (Stage) returnToGeneralkaButton.getScene().getWindow();
                stage.setScene(generalkaScene);
        }
}