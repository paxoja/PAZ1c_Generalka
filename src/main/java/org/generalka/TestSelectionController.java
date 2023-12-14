package org.generalka;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.generalka.table.OverviewManager;
import org.generalka.table.OverviewManagerImpl;
import org.generalka.table.TestOverview;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

    ObservableList<TestOverview> testOverviewObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            ObservableList<Integer> years = FXCollections.observableArrayList(1, 2, 3);
            yearOfStudyComboBox.setItems(years);

            ObservableList<String> subjects = FXCollections.observableArrayList("UGR1", "NUM", "MZI", "UGR1", "SXM1", "PAZ1c", "OSY1", "DGS", "BDS1a", "MSU", "DSM3a");
            subjectComboBox.getItems().addAll(subjects);

            ObservableList<String> semesters = FXCollections.observableArrayList("winter", "summer");
            semesterComboBox.setItems(semesters);
            // Get the test summary from the OverviewManager
            List<TestOverview> testOverviews = overviewManager.getTestSummary();

            testOverviewObservableList.addAll(testOverviews);

            // Set the items in the TableView
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
