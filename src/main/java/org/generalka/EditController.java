package org.generalka;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.generalka.storage.*;
import org.generalka.table.CenterTextTable;
import org.generalka.table.OverviewManager;
import org.generalka.table.OverviewManagerImpl;
import org.generalka.table.TestOverview;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EditController {

    @FXML
    private TableView<TestOverview> TestTable;

    @FXML
    private TableColumn<TestOverview, Long> idColmn;

    @FXML
    private TableColumn<TestOverview, String> nameColmn;

    @FXML
    private TableColumn<TestOverview, String> semesterColmn;

    @FXML
    private TableColumn<TestOverview, String> subjectColmn;

    @FXML
    private TableColumn<TestOverview, Integer> yearOfStudyColmn;

    @FXML
    private ComboBox<Integer> yearOfStudyComboBox;

    @FXML
    private ComboBox<String> subjectComboBox;

    @FXML
    private ComboBox<String> semesterComboBox;

    @FXML
    private CheckBox wholeSemesterCheckbox;

    @FXML
    private Button returnToGeneralkaButton;

    @FXML
    private Button DeleteTestButton;

    @FXML
    public Button EditTestButton;

    private final OverviewManager overviewManager = new OverviewManagerImpl();

    private TestDao testDao = DaoFactory.INSTANCE.getTestDao();

    ObservableList<TestOverview> testOverviewObservableList = FXCollections.observableArrayList();

    private List<TestOverview> originalTestOverviews;

    // variable to store the selected test
    private TestOverview selectedTest;
    private void setSelectedTest(TestOverview test) {
        this.selectedTest = test;
    }

    @FXML
    public void initialize() {
        // setting the table
        idColmn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColmn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        semesterColmn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        subjectColmn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        yearOfStudyColmn.setCellValueFactory(new PropertyValueFactory<>("yearOfStudy"));//

        // center text
        idColmn.setCellFactory(new CenterTextTable<>());
        semesterColmn.setCellFactory(new CenterTextTable<>());
        subjectColmn.setCellFactory(new CenterTextTable<>());
        yearOfStudyColmn.setCellFactory(new CenterTextTable<>());

        // set up the row click event handler
        TestTable.setRowFactory(tv -> {
            TableRow<TestOverview> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    TestOverview selectedTest = row.getItem();
                    // store the selected test to open or delete it
                    setSelectedTest(selectedTest);
                }
            });
            return row;
        });

        try {
            // test summary from OverviewManager
            List<TestOverview> testOverviews = overviewManager.getTestSummary();
            testOverviewObservableList.addAll(testOverviews);

            // sets items to test table
            TestTable.setItems(testOverviewObservableList);

            // list for saving during filtring
            originalTestOverviews = overviewManager.getTestSummary();

            // remove duplicites
            testOverviewObservableList.clear();

            // adding
            testOverviewObservableList.addAll(originalTestOverviews);

            // setting items
            TestTable.setItems(testOverviewObservableList);

        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        ObservableList<Integer> years = FXCollections.observableArrayList(1, 2, 3);
        yearOfStudyComboBox.setItems(years);

        ObservableList<String> subjects = FXCollections.observableArrayList("UGR1", "NUM", "MZI", "UGR1", "SXM1", "PAZ1c", "OSY1", "DGS", "BDS1a", "MSU", "DSM3a");
        subjectComboBox.getItems().addAll(subjects);

        ObservableList<String> semesters = FXCollections.observableArrayList("winter", "summer");
        semesterComboBox.setItems(semesters);
    }

    @FXML
    void chooseWholeSemester() {
        applyFilters();
    }

    @FXML
    private void handleYearOfStudyChange() {
        applyFilters();
    }

    @FXML
    private void handleSubjectChange() {
        applyFilters();
    }

    @FXML
    private void handleSemesterChange() {
        applyFilters();
    }

    private void applyFilters() {
        Integer selectedYearOfStudy = yearOfStudyComboBox.getValue();
        String selectedSubject = subjectComboBox.getValue();
        String selectedSemester = semesterComboBox.getValue();
        boolean wholeSemesterSelected = wholeSemesterCheckbox.isSelected();

        List<TestOverview> filteredList = originalTestOverviews
                .stream()
                .filter(testOverview ->
                        (selectedYearOfStudy == null || testOverview.getYearOfStudy() == selectedYearOfStudy) &&
                                (selectedSubject == null || testOverview.getSubject().equals(selectedSubject)) &&
                                (selectedSemester == null || testOverview.getSemester().equals(selectedSemester)) &&
                                (!wholeSemesterSelected || testOverview.isWholeSemester()))
                .collect(Collectors.toList());

        testOverviewObservableList.clear();
        testOverviewObservableList.addAll(filteredList);
    }

    // return to main screen
    @FXML
    private void returnToGeneralka() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Profile.fxml"));
        Parent parent = loader.load();
        Scene generalkaScene = new Scene(parent);
        generalkaScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) returnToGeneralkaButton.getScene().getWindow();
        stage.setScene(generalkaScene);
    }

    @FXML
    private void deleteTest() {
        UserDao userDao = DaoFactory.INSTANCE.getUserDao();
        Optional<User> currentUserOptional = userDao.getCurrentUser();
        TestOverview selectedTest = TestTable.getSelectionModel().getSelectedItem();

        if (currentUserOptional.isPresent() && selectedTest != null) {
            User currentUser = currentUserOptional.get();

            try {
                // Fetch the complete Test object from the database based on the selectedTest's ID
                Test test = testDao.getTestById(selectedTest.getId());

                if (currentUser.isAdmin() || test.getUser().getId() == currentUser.getId()) {
                    // Allow test deletion for admin users or if the test belongs to the current user
                    testDao.deleteTest(selectedTest.getId());

                    // Refresh table
                    originalTestOverviews = overviewManager.getTestSummary();
                    testOverviewObservableList.clear();
                    testOverviewObservableList.addAll(originalTestOverviews);
                    TestTable.setItems(testOverviewObservableList);
                } else {
                    // Display a message or handle unauthorized deletion...
                    System.out.println("You are not authorized to delete this test.");
                }
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
                // Handle the case where the test cannot be found in the database
            }
        } else {
            // Handle the case where the current user is not logged in or no test is selected
            // For example:
            System.out.println("No test selected or user not logged in.");
        }
    }


    @FXML
    private void editTest() throws IOException {
        // Ensure a test is selected
        if (selectedTest != null) {
            // Fetch the corresponding Test object for editing
            Test testToEdit = fetchTest(selectedTest.getId());

            // Navigate to TestAttributesController for editing
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TestEditAttributes.fxml"));
            Parent parent = loader.load();
            TestEditAttributesController testEditAttributesController = loader.getController();

            // Set the selected test for editing
            testEditAttributesController.setTestForEditing(testToEdit);

            Scene editTestScene = new Scene(parent);
            editTestScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            Stage stage = (Stage) EditTestButton.getScene().getWindow();
            stage.setScene(editTestScene);
        } else {
            // If no test is selected, show a message or handle the case as needed
            System.out.println("No test selected for editing.");
        }
    }

    // Helper method to fetch Test object based on TestOverview id
    private Test fetchTest(Long testId) {
        try {
            return testDao.getTestById(testId);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            // Handle the exception appropriately
            return null;
        }
    }

}


