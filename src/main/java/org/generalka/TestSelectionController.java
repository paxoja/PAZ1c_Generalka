package org.generalka;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.generalka.storage.DaoFactory;
import org.generalka.storage.EntityNotFoundException;
import org.generalka.storage.TestQuestion;
import org.generalka.storage.TestQuestionDao;
import org.generalka.storage.TestQuestionDaoImpl;
import org.generalka.table.OverviewManager;
import org.generalka.table.OverviewManagerImpl;
import org.generalka.table.TestOverview;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TestSelectionController implements Initializable {

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
    private Button returnToGeneralkaButton;

    private OverviewManager overviewManager = new OverviewManagerImpl();
    private TestQuestionDao testQuestionDao = new TestQuestionDaoImpl();

    ObservableList<TestOverview> testOverviewObservableList = FXCollections.observableArrayList();

    private List<TestOverview> originalTestOverviews;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            ObservableList<Integer> years = FXCollections.observableArrayList(1, 2, 3);
            yearOfStudyComboBox.setItems(years);

            ObservableList<String> subjects = FXCollections.observableArrayList("UGR1", "NUM", "MZI", "UGR1", "SXM1", "PAZ1c", "OSY1", "DGS", "BDS1a", "MSU", "DSM3a");
            subjectComboBox.getItems().addAll(subjects);

            ObservableList<String> semesters = FXCollections.observableArrayList("winter", "summer");
            semesterComboBox.setItems(semesters);
            // test summary z OverviewManager
            List<TestOverview> testOverviews = overviewManager.getTestSummary();

            testOverviewObservableList.addAll(testOverviews);

            // nastavia sa veci do TestTable
            TestTable.setItems(testOverviewObservableList);

            // zoznam na ulozenie pri filtrovani - trebalo ho lebo sme sa nevedeli pri zvoleni filtru potom pri inom filtre dostat k inemu predmetu
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

        idColmn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColmn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        semesterColmn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        subjectColmn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        yearOfStudyColmn.setCellValueFactory(new PropertyValueFactory<>("yearOfStudy"));
    }

    // aplikuje filtrovanie pre roky
    @FXML
    private void handleYearOfStudyChange(ActionEvent event) {
        applyFilters();
    }

    // aplikuje filtrovanie pre predmety
    @FXML
    private void handleSubjectChange(ActionEvent event) {
        applyFilters();
    }

    // aplikuje filtrovanie pre semester
    @FXML
    private void handleSemesterChange(ActionEvent event) {
        applyFilters();
    }

    private void applyFilters() {
        Integer selectedYearOfStudy = yearOfStudyComboBox.getValue();
        String selectedSubject = subjectComboBox.getValue();
        String selectedSemester = semesterComboBox.getValue();

        List<TestOverview> filteredList = originalTestOverviews
                .stream()
                .filter(testOverview ->
                        (selectedYearOfStudy == null || testOverview.getYearOfStudy() == selectedYearOfStudy) &&
                                (selectedSubject == null || testOverview.getSubject().equals(selectedSubject)) &&
                                (selectedSemester == null || testOverview.getSemester().equals(selectedSemester)))
                .collect(Collectors.toList());

        // vzdy ked menime filter z jedneho na druhy tak najprv vymaze vsetko a potom prida nanovo prefiltrovane veci
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
        Stage stage = (Stage) returnToGeneralkaButton.getScene().getWindow();
        stage.setScene(generalkaScene);
    }


    // otvorenie test sceny
    @FXML
    private void openTestScene() {
        TestOverview selectedTest = TestTable.getSelectionModel().getSelectedItem();

        if (selectedTest != null) {

            // gettneme otazky podla test id
            List<TestQuestion> testQuestions = testQuestionDao.getTestQuestionsByTestId(selectedTest.getId());

            try {
                // otvori sa TestController a preda sa mu test ID
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Test.fxml"));
                Parent parent = loader.load();
                TestController testController = loader.getController();
                testController.setTestId(selectedTest.getId());

                Scene testScene = new Scene(parent);
                Stage stage = (Stage) TestTable.getScene().getWindow();
                stage.setScene(testScene);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
