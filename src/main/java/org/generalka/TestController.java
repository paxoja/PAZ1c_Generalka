package org.generalka;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.generalka.storage.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class TestController {

    @FXML
    private Label questionLabel;

    @FXML
    private VBox answersContainer;

    private List<TestQuestion> testQuestions;

    @FXML
    private Button returnToTestSelectionButton;

    private Long testId;

    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();
    private TestDao testDao = DaoFactory.INSTANCE.getTestDao();
    private TestQuestionDao testQuestionDao = DaoFactory.INSTANCE.getTestQuestionDao();
    private AnswerDao answerDao = DaoFactory.INSTANCE.getAnswerDao();
    private TestHistoryDao testHistoryDao = DaoFactory.INSTANCE.getTestHistoryDao();
    private int currentQuestionIndex = 0;

    private Map<Answer, RadioButton> userAnswersMap;

    private List<Answer> userAnswers;


    @FXML
    public void initialize() {
        userAnswersMap = new HashMap<>();
        userAnswers = new ArrayList<>();
        if (testId != null) {
            loadTestQuestions();
        }
    }

    public void setTestId(Long testId) {
        this.testId = testId;
        loadTestQuestions();
    }

    private void loadTestQuestions() {
        testQuestions = testQuestionDao.getTestQuestionsByTestId(testId);

        if (testQuestions != null && !testQuestions.isEmpty()) {
            loadQuestionAndAnswers();
        } else {
            questionLabel.setText("No questions found for the test.");
            answersContainer.getChildren().clear();
        }
    }

    private void loadQuestionAndAnswers() {
        if (currentQuestionIndex < testQuestions.size()) {
            TestQuestion currentQuestion = testQuestions.get(currentQuestionIndex);
            questionLabel.setText(currentQuestion.getQuestion());

            answersContainer.getChildren().clear();

            // Check if answers are not null before iterating
            List<Answer> answers = answerDao.getAnswersByQuestionId(currentQuestion.getId());

            if (answers != null && !answers.isEmpty()) {
                ToggleGroup toggleGroup = new ToggleGroup();

                for (Answer answer : answers) {
                    RadioButton radioButton = new RadioButton(answer.getAnswer());
                    radioButton.setToggleGroup(toggleGroup);

                    // Set margin between radio buttons
                    VBox.setMargin(radioButton, new Insets(5, 0, 5, 0));

                    radioButton.setOnAction(event -> {
                        // Print statements for debugging
                        System.out.println("RadioButton selected: " + radioButton.isSelected());
                        System.out.println("Answer: " + answer.getAnswer());

                        // Update userAnswersMap when the user selects an answer
                        if (radioButton.isSelected()) {
                            userAnswersMap.put(answer, radioButton);
                            userAnswers.add(answer);
                        } else {
                            userAnswersMap.remove(answer);
                            userAnswers.remove(answer);
                        }

                        // Print userAnswersMap for debugging
                        System.out.println("User Answers Map: " + userAnswersMap);
                    });

                    answersContainer.getChildren().add(radioButton);
                }
            } else {
                // Handle the case where answers are null or empty
                // You might want to display a message or take appropriate action
            }
        } else {
            questionLabel.setText("End of the test.");
            answersContainer.getChildren().clear();
            userAnswersMap = null; // Clear userAnswersMap when reaching the end of the test
        }
    }



    @FXML
    private void handleSubmit() throws IOException {
        int score = calculateScore();
        saveTestHistory(score);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/TestSelection.fxml"));
        Parent parent = loader.load();
        Scene testSelectionScene = new Scene(parent);
        Stage stage = (Stage) returnToTestSelectionButton.getScene().getWindow();
        stage.setScene(testSelectionScene);
    }

    @FXML
    void nextQuestion() {
        currentQuestionIndex++;
        loadQuestionAndAnswers();
    }

    private int calculateScore() {
        if (userAnswers == null) {
            return 0;
        }

        // Count correct answers among user-selected answers
        long correctAnswersCount = userAnswers.stream().filter(Answer::getIsCorrect).count();

        // Calculate and return the score
        return (int) correctAnswersCount;
    }

    private void saveTestHistory(int score) {
        // Create a TestHistory object and save it using the TestHistoryDao
        TestHistory testHistory = new TestHistory();
        testHistory.setScore(score);
        testHistory.setTest(testDao.getTestById(testId));
        // Set the user - you may need to get the actual user from your application
        Optional<User> currentUser = userDao.getCurrentUser();
        testHistory.setUser(currentUser.get());

        testHistoryDao.saveTestHistory(testHistory);
    }

    @FXML
    void returnToTestSelection() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/TestSelection.fxml"));
        Parent parent = loader.load();
        Scene testSelectionScene = new Scene(parent);
        Stage stage = (Stage) returnToTestSelectionButton.getScene().getWindow();
        stage.setScene(testSelectionScene);
    }
}
