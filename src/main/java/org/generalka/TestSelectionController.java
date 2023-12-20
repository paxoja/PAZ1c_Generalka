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
import org.generalka.table.CenterTextTable;

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


    ObservableList<TestOverview> testOverviewObservableList = FXCollections.observableArrayList();

    private List<TestOverview> originalTestOverviews;

    @FXML
    public void initialize() {
        // setting names for headers
        idColmn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColmn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        semesterColmn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        subjectColmn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        yearOfStudyColmn.setCellValueFactory(new PropertyValueFactory<>("yearOfStudy"));

        idColmn.setCellFactory(new CenterTextTable<>());
        semesterColmn.setCellFactory(new CenterTextTable<>());
        subjectColmn.setCellFactory(new CenterTextTable<>());
        yearOfStudyColmn.setCellFactory(new CenterTextTable<>());

        try {
            // test summary from OverviewManager
            List<TestOverview> testOverviews = overviewManager.getTestSummary();
            testOverviewObservableList.addAll(testOverviews);

            // set items to the TestTable
            TestTable.setItems(testOverviewObservableList);

            // saving in filtering
            originalTestOverviews = overviewManager.getTestSummary();

            // removing duplicites
            testOverviewObservableList.clear();

            // adding subjects
            testOverviewObservableList.addAll(originalTestOverviews);

            // set subjects
            TestTable.setItems(testOverviewObservableList);

        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        // lists to fill the comboboxes
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

    //
    private void applyFilters() {
        Integer selectedYearOfStudy = yearOfStudyComboBox.getValue();
        String selectedSubject = subjectComboBox.getValue();
        String selectedSemester = semesterComboBox.getValue();
        boolean wholeSemesterSelected = wholeSemesterCheckbox.isSelected();

        // if the parameters are not null it shows the filtered results
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
                getClass().getResource("/generalka.fxml"));
        Parent parent = loader.load();
        Scene generalkaScene = new Scene(parent);
        generalkaScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) returnToGeneralkaButton.getScene().getWindow();
        stage.setScene(generalkaScene);
    }


    // opening the test scene
    @FXML
    private void openTestScene() {
        TestOverview selectedTest = TestTable.getSelectionModel().getSelectedItem();

        if (selectedTest != null) {

            // getting questions based on the test id
            testQuestionDao.getTestQuestionsByTestId(selectedTest.getId());

            try {
                // TestController opens and retrieves test ID which it shows
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
}
