package org.generalka;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.generalka.storage.*;
import org.generalka.table.OverviewManager;
import org.generalka.table.OverviewManagerImpl;
import org.generalka.table.TestOverview;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestSelectionController {

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
    private Button adminDeleteTestButton;

    private final OverviewManager overviewManager = new OverviewManagerImpl();
    private final TestQuestionDao testQuestionDao = new TestQuestionDaoImpl();

    private TestDao testDao = DaoFactory.INSTANCE.getTestDao();

    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();

    ObservableList<TestOverview> testOverviewObservableList = FXCollections.observableArrayList();

    private List<TestOverview> originalTestOverviews;

    @FXML
    public void initialize() {

        Optional<User> currentUser = userDao.getCurrentUser();
        if (currentUser.isPresent() && currentUser.get().isAdmin()) {
            adminDeleteTestButton.setVisible(true);
        } else {
            adminDeleteTestButton.setVisible(false);
        }
        // nastavenie tabulky - vytvorenie nazvov pre stlpce
        idColmn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColmn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        semesterColmn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        subjectColmn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        yearOfStudyColmn.setCellValueFactory(new PropertyValueFactory<>("yearOfStudy"));

        try {
            // test summary z OverviewManager
            List<TestOverview> testOverviews = overviewManager.getTestSummary();
            testOverviewObservableList.addAll(testOverviews);

            // nastavia sa veci do TestTable
            TestTable.setItems(testOverviewObservableList);

            // zoznam na ulozenie pri filtrovani
            originalTestOverviews = overviewManager.getTestSummary();

            // odstranenie duplicit
            testOverviewObservableList.clear();

            // pridanie predmetov
            testOverviewObservableList.addAll(originalTestOverviews);

            // set predmety
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

    // prechod spat na main screen
    @FXML
    private void returnToGeneralka() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/generalka.fxml"));
        Parent parent = loader.load();
        Scene generalkaScene = new Scene(parent);
        generalkaScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) returnToGeneralkaButton.getScene().getWindow();
        stage.setScene(generalkaScene);
    }


    // otvorenie test sceny
    @FXML
    private void openTestScene() {
        TestOverview selectedTest = TestTable.getSelectionModel().getSelectedItem();

        if (selectedTest != null) {

            // gettneme otazky podla test id
            testQuestionDao.getTestQuestionsByTestId(selectedTest.getId());

            try {
                // otvori sa TestController a preda sa mu test ID
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Test.fxml"));
                Parent parent = loader.load();
                TestController testController = loader.getController();
                testController.setTestId(selectedTest.getId());


                Scene testScene = new Scene(parent);
                testScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
                Stage stage = (Stage) TestTable.getScene().getWindow();
                stage.setScene(testScene);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteTest() {
        // Implement test deletion logic here
        TestOverview selectedTest = TestTable.getSelectionModel().getSelectedItem();
        if (selectedTest != null) {
            try {
                // Delete the test using the appropriate DAO method
                testDao.deleteTest(selectedTest.getId());

                // Refresh the table after deletion
                originalTestOverviews = overviewManager.getTestSummary();
                testOverviewObservableList.clear();
                testOverviewObservableList.addAll(originalTestOverviews);
                TestTable.setItems(testOverviewObservableList);

            } catch (EntityNotFoundException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
        }
    }
}
