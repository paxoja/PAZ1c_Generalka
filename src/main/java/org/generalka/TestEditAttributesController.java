package org.generalka;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.generalka.storage.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

public class TestEditAttributesController {

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

    private String topic;
    private Integer year;
    private String subject;
    private String semester;
    private boolean isWholeSemester;

    private TestDao testDao = DaoFactory.INSTANCE.getTestDao();
    private TestFxModel testFxModel;

    private Test currentTest; // Field to store the current test

    public TestEditAttributesController() {
        testFxModel = new TestFxModel();
    }

    @FXML
    private void initialize() {
        // Initialize ComboBoxes with data
        ObservableList<Integer> years = FXCollections.observableArrayList(1, 2, 3);
        yearComboBox.setItems(years);

        ObservableList<String> subjects = FXCollections.observableArrayList("UGR1", "NUM", "MZI", "UGR1", "SXM1", "PAZ1c", "OSY1", "DGS", "BDS1a", "MSU", "DSM3a");
        subjectComboBox.getItems().addAll(subjects);

        ObservableList<String> semesters = FXCollections.observableArrayList("winter", "summer");
        semesterComboBox.setItems(semesters);

        // Add change listeners to update the current test object
        descriptionTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (currentTest != null) {
                currentTest.setTopic(newValue);
                updateTestEditAttributeInDatabase(currentTest.getId(), "topic", newValue);
            }
        });

        yearComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (currentTest != null) {
                currentTest.setYearOfStudy(newValue);
                updateTestEditAttributeInDatabase(currentTest.getId(), "year_of_study", newValue);
            }
        });

        subjectComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (currentTest != null) {
                currentTest.setSubject(newValue);
                updateTestEditAttributeInDatabase(currentTest.getId(), "subject", newValue);
            }
        });

        semesterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (currentTest != null) {
                currentTest.setSemester(newValue);
                updateTestEditAttributeInDatabase(currentTest.getId(), "semester", newValue);
            }
        });

        wholeSemesterCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (currentTest != null) {
                currentTest.setIsWholeSemester(newValue);
                updateTestEditAttributeInDatabase(currentTest.getId(), "is_whole_semester", newValue);
            }
        });
    }

    private void updateTestEditAttributeInDatabase(Long testId, String attributeName, Object attributeValue) {
        try {
            testDao.updateTestEditAttribute(testId, attributeName, attributeValue);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @FXML
    private void moveToCreateTest() throws IOException {
        topic = descriptionTextField.getText();
        year = yearComboBox.getValue();
        subject = subjectComboBox.getValue();
        semester = semesterComboBox.getValue();
        isWholeSemester = wholeSemesterCheckBox.isSelected();

        // If currentTest is not null, we are editing an existing test
        if (currentTest != null) {
            currentTest.setTopic(topic);
            currentTest.setYearOfStudy(year);
            currentTest.setSubject(subject);
            currentTest.setSemester(semester);
            currentTest.setIsWholeSemester(isWholeSemester);

            navigateToTestCreator(currentTest);
        } else {
            // If currentTest is null, we are creating a new test
            Test test = createTest();
            if (test != null) {
                navigateToTestCreator(test);
            }
        }

        // Update the filter values on the existing instance of TestAttributesController
        updateFilterValues(topic, year, subject, semester, isWholeSemester, this);
    }

    private void updateFilterValues(String topic, Integer year, String subject, String semester, boolean isWholeSemester, TestEditAttributesController controller) {
        // Update the filter values on the existing instance of TestAttributesController
        controller.setFilterValues(topic, year, subject, semester, isWholeSemester, currentTest);
    }


    private void navigateToTestCreator(Test test) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TestEdit.fxml"));
        Parent parent = loader.load();
        TestEditController testEditController = loader.getController();
        testEditController.setTest(test); // Pass the currentTest object
        Scene createTestScene = new Scene(parent);
        createTestScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) moveToCreateTestButton.getScene().getWindow();
        stage.setScene(createTestScene);
    }





    private Test createTest() {
        Test test = new Test();
        test.setTopic(descriptionTextField.getText());
        test.setYearOfStudy(yearComboBox.getValue());
        test.setSubject(subjectComboBox.getValue());
        test.setSemester(semesterComboBox.getValue());
        test.setIsWholeSemester(wholeSemesterCheckBox.isSelected());
        test.setDate(new Timestamp(System.currentTimeMillis()));

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



    @FXML
    private void returnToGeneralka() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/generalka.fxml"));
        Parent parent = loader.load();
        Scene generalkaScene = new Scene(parent);
        generalkaScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) returnToGeneralkaButton.getScene().getWindow();
        stage.setScene(generalkaScene);
    }

    public void setFilterValues(String topic, Integer year, String subject, String semester, boolean isWholeSemester, Test currentTest) {
        descriptionTextField.setText(topic);
        yearComboBox.setValue(year);
        subjectComboBox.setValue(subject);
        semesterComboBox.setValue(semester);
        wholeSemesterCheckBox.setSelected(isWholeSemester);
        this.currentTest = currentTest; // Set the current test
    }

    public void setTestForEditing(Test test) {
        // Set the filter values for editing the provided Test object
        descriptionTextField.setText(test.getTopic());
        yearComboBox.setValue(test.getYearOfStudy());
        subjectComboBox.setValue(test.getSubject());
        semesterComboBox.setValue(test.getSemester());
        wholeSemesterCheckBox.setSelected(test.getIsWholeSemester());
        currentTest = test; // Set the current test
    }

}
