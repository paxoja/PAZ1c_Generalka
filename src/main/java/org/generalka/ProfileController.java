package org.generalka;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.generalka.storage.*;
import org.generalka.table.OverviewManagerImpl;
import org.generalka.table.TestHistoryProfile;
import org.generalka.table.CenterTextTable;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class ProfileController {

    @FXML
    private Button adminEdit;

    @FXML
    private Label profileUsername;

    @FXML
    private Button returnToGeneralkaButton;

    @FXML
    private TableView<TestHistoryProfile> testHistoryTable;

    @FXML
    private TableColumn<TestHistoryProfile, Long> historyIdColmn;

    @FXML
    private TableColumn<TestHistoryProfile, String> historyTopicColmn;

    @FXML
    private TableColumn<TestHistoryProfile, String> historySubjectColmn;

    @FXML
    private TableColumn<TestHistoryProfile, String> historySemesterColmn;

    @FXML
    private TableColumn<TestHistoryProfile, Integer> historyYearOfStudyColmn;

    @FXML
    private TableColumn<TestHistoryProfile, String> historyScoreColmn;

    @FXML
    private TableColumn<TestHistoryProfile, Timestamp> historyDateColmn;

    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();
    private OverviewManagerImpl overviewManager = new OverviewManagerImpl();


    // after loading FXML we retrieve the current user from DAO and sets his username
    // we use Optional, which finds the presence of the object through getter
    @FXML
    private void initialize() {
        Optional<User> currentUser = userDao.getCurrentUser();
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            profileUsername.setText(user.getUsername());
        }
        if (currentUser.isPresent() && currentUser.get().isAdmin()) {
            adminEdit.setVisible(true);
        } else {
            adminEdit.setVisible(false);
        }

        historyIdColmn.setCellValueFactory(new PropertyValueFactory<>("id"));
        historyTopicColmn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        historySubjectColmn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        historySemesterColmn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        historyYearOfStudyColmn.setCellValueFactory(new PropertyValueFactory<>("yearOfStudy"));
        historyScoreColmn.setCellValueFactory(new PropertyValueFactory<>("scoreString"));
        historyDateColmn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));

        historyIdColmn.setCellFactory(new CenterTextTable<>());
        historySubjectColmn.setCellFactory(new CenterTextTable<>());
        historySemesterColmn.setCellFactory(new CenterTextTable<>());
        historyYearOfStudyColmn.setCellFactory(new CenterTextTable<>());
        historyScoreColmn.setCellFactory(new CenterTextTable<>());
        historyDateColmn.setCellFactory(new CenterTextTable<>());

        try {
            List<TestHistoryProfile> testHistoryProfiles = overviewManager.getHistorySummary();
            ObservableList<TestHistoryProfile> testHistoryObservableList = FXCollections.observableArrayList(testHistoryProfiles);
            testHistoryTable.setItems(testHistoryObservableList);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    // if user is admin, he can delete others users tests
    @FXML
    void moveToEdit() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/AdminEdit.fxml"));
        Parent parent = loader.load();
        Scene adminScene = new Scene(parent);
        adminScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) adminEdit.getScene().getWindow();
        stage.setScene(adminScene);

    }

    // move to main screen
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

}