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
import org.generalka.storage.Answer;
import org.generalka.storage.AnswerDao;
import org.generalka.storage.DaoFactory;
import org.generalka.storage.TestQuestion;
import org.generalka.table.TestResultItem;

import java.io.IOException;
import java.util.List;

public class ShowTestResultController {

    @FXML
    private Label scoreLabel;

    @FXML
    private TableView<TestResultItem> resultsTable;

    @FXML
    private Button returnToGeneralkaButton;

    private AnswerDao answerDao = DaoFactory.INSTANCE.getAnswerDao();

    private ObservableList<TestResultItem> testResultObservableList = FXCollections.observableArrayList();

    private TestController testController;
    private List<Answer> userAnswers;
    private List<TestQuestion> testQuestions;


    public void setTestController(TestController testController) {
        this.testController = testController;

        System.out.println("TestController Reference: " + testController);

        // Check if the TestController instance is set
        if (this.testController == null) {
            System.err.println("TestController instance is null. Aborting table initialization.");
            return;
        }

        // Ensure that the TestController has loaded questions and answers
        userAnswers = this.testController.getUserAnswers();
        testQuestions = this.testController.getTestQuestions();
        if (testQuestions == null || userAnswers == null) {
            System.err.println("TestController has not loaded questions and answers. Aborting table initialization.");
            return;
        }

        // Load the table with test results
        loadTestResults();
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

    @FXML
    public void initialize() {

        System.out.println("TestController Reference in initialize: " + testController);
        TableColumn<TestResultItem, Integer> questionNumberCol = new TableColumn<>("Question Number");
        TableColumn<TestResultItem, String> userAnswerCol = new TableColumn<>("Your Answer");
        TableColumn<TestResultItem, String> correctAnswerCol = new TableColumn<>("Correct Answer");

        questionNumberCol.setCellValueFactory(new PropertyValueFactory<>("questionNumber"));
        userAnswerCol.setCellValueFactory(new PropertyValueFactory<>("userAnswer"));
        correctAnswerCol.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));

    }

    private void loadTestResults() {
        // Initialize the total correct answers count
        testResultObservableList.clear();
        int totalCorrectAnswers = 0;

        // Iterate through each test question and get the user's answers and correct answers
        for (int i = 0; i < testQuestions.size(); i++) {
            TestQuestion testQuestion = testQuestions.get(i);

            // Get the user's answer for the current question
            Answer userAnswer = (i < userAnswers.size()) ? userAnswers.get(i) : null;

            // Get the correct answer for the current question
            Answer correctAnswer = answerDao.getCorrectAnswerByQuestionId(testQuestion.getId());

            // Create a TestResultRow object
            TestResultItem testResultRow = new TestResultItem(i + 1,
                    (userAnswer != null) ? userAnswer.getAnswer() : "Not Answered",
                    correctAnswer.getAnswer());

            // Add the row to the observable list
            testResultObservableList.add(testResultRow);

            // Update the total correct answers count
            if (userAnswer != null && userAnswer.getIsCorrect()) {
                totalCorrectAnswers++;
            }
        }

        // Display the total correct answers count
        scoreLabel.setText("Total Correct Answers: " + totalCorrectAnswers + " From Total Of: " + testQuestions.size());

        // Set the items in the table
        resultsTable.setItems(testResultObservableList);
    }

}