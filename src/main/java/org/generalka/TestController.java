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
import java.util.*;

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

    private Map<Integer, Answer> selectedAnswersMap;
    private Map<Integer, Answer> userAnswersMap;

    @FXML
    public void initialize() {
        selectedAnswersMap = new HashMap<>();
        userAnswersMap = new HashMap<>();
        if (testId != null) {
            loadTestQuestions();
        }
    }

    public void setTestId(Long testId) {
        this.testId = testId;
        loadTestQuestions();
    }

    public List<Answer> getUserAnswers() {
        // Convert userAnswersMap values to a List
        return new ArrayList<>(userAnswersMap.values());
    }

    public List<TestQuestion> getTestQuestions() {
        return testQuestions;
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

                    // Update selectedAnswer when the user selects an answer
                    radioButton.setOnAction(event -> {
                        selectedAnswersMap.put(currentQuestionIndex, answer);
                    });

                    answersContainer.getChildren().add(radioButton);
                }

                // Check if there is a selected answer for the current question
                if (selectedAnswersMap.containsKey(currentQuestionIndex)) {
                    Answer selectedAnswer = selectedAnswersMap.get(currentQuestionIndex);

                }
            } else {
                // Handle the case where answers are null or empty
                // You might want to display a message or take appropriate action
            }
        } else {
            questionLabel.setText("End of the test.");
            answersContainer.getChildren().clear();
            selectedAnswersMap = null; // Clear selectedAnswersMap when reaching the end of the test
        }
    }



    @FXML
    private void handleSubmit() throws IOException {
        // Iterate over the selectedAnswersMap entries and add them to the userAnswersMap
        if (selectedAnswersMap == null) {
            selectedAnswersMap = new HashMap<>();
        }

        // Iterate through the userAnswersMap and update selectedAnswersMap
        for (Map.Entry<Integer, Answer> entry : userAnswersMap.entrySet()) {
            int index = entry.getKey();
            Answer answer = entry.getValue();
            selectedAnswersMap.put(index, answer);
        }

        int score = calculateScore();
        saveTestHistory(score);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowTestResult.fxml"));
        Parent parent = loader.load();
        ShowTestResultController showTestResultController = loader.getController();

        // Pass the current TestController instance
        showTestResultController.setTestController(this);

        Scene showTestResultScene = new Scene(parent);
        Stage stage = (Stage) returnToTestSelectionButton.getScene().getWindow();
        stage.setScene(showTestResultScene);

        // Set the controller to null to ensure a new instance is created next time
        loader.setController(null);
    }

    @FXML
    void nextQuestion() {
        if (selectedAnswersMap.containsKey(currentQuestionIndex)) {
            Answer previousAnswer = userAnswersMap.get(currentQuestionIndex);

            // If there was a previous answer for the current question, remove it
            if (previousAnswer != null) {
                userAnswersMap.remove(currentQuestionIndex);
            }

            // Add the selected answer to the userAnswersMap
            userAnswersMap.put(currentQuestionIndex, selectedAnswersMap.get(currentQuestionIndex));
        }

        // Proceed to the next question
        currentQuestionIndex++;
        loadQuestionAndAnswers();
    }

    @FXML
    void questionBefore() {
        if (currentQuestionIndex > 0) {
            // Update selected answer when going back to the previous question
            selectedAnswersMap.put(currentQuestionIndex, null);

            // Remove the current answer from userAnswersMap if it exists
            userAnswersMap.remove(currentQuestionIndex);

            currentQuestionIndex--;
            loadQuestionAndAnswers();
        } else {
            // Handle the case where there are no previous questions (e.g., user is on the first question)
            System.out.println("No previous question available");
        }
    }

    private int calculateScore() {
        if (userAnswersMap == null) {
            return 0;
        }

        // Count correct answers among user-selected answers
        long correctAnswersCount = userAnswersMap.values().stream().filter(Answer::getIsCorrect).count();

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
