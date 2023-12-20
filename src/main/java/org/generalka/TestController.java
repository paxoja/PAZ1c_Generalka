package org.generalka;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.generalka.storage.*;

import java.io.IOException;
import java.util.*;

public class TestController {


    @FXML
    private Label questionLabel;

    @FXML
    private Label endOfTestLabel;

    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;

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

    // initializing the hashmaps
    @FXML
    public void initialize() {
        selectedAnswersMap = new HashMap<>();
        userAnswersMap = new HashMap<>();
        if (testId != null) {
            loadTestQuestions();
        }
    }

    // sets which test we are doing
    public void setTestId(Long testId) {
        this.testId = testId;
        loadTestQuestions();
    }

    // answers which are in test, which the user chose - we use in creating score
    public List<Answer> getUserAnswers() {
        // convert userAnswersMap values to a List
        return new ArrayList<>(userAnswersMap.values());
    }

    // questions in test
    public List<TestQuestion> getTestQuestions() {
        return testQuestions;
    }

    // we choose questions based on test id
    private void loadTestQuestions() {
        testQuestions = testQuestionDao.getTestQuestionsByTestId(testId);

        // if questions arent null its loaded
        if (testQuestions != null && !testQuestions.isEmpty()) {
            loadQuestionAndAnswers();
        } else {
            questionLabel.setText("No questions found for the test.");
            answersContainer.getChildren().clear();
        }
    }



    private void loadQuestionAndAnswers() {

        // loads current question in test
        if (currentQuestionIndex < testQuestions.size()) {
            TestQuestion currentQuestion = testQuestions.get(currentQuestionIndex);
            questionLabel.setText(currentQuestion.getQuestion());

            // cleans when we go to next question
            answersContainer.getChildren().clear();

            // checks if answers are not null before iterating
            List<Answer> answers = answerDao.getAnswersByQuestionId(currentQuestion.getId());

            if (answers != null && !answers.isEmpty()) {

                // group of answers - circles
                ToggleGroup toggleGroup = new ToggleGroup();

                // cycle which creates radiobutton with the answers
                for (Answer answer : answers) {
                    RadioButton radioButton = new RadioButton(answer.getAnswer());
                    radioButton.setToggleGroup(toggleGroup);

                    // setting margin between radio buttons
                    VBox.setMargin(radioButton, new Insets(5, 0, 5, 0));

                    // update selectedAnswer when the user selects an answer
                    // when selected, it saves the index of the answer with the answer
                    radioButton.setOnAction(event -> {
                        selectedAnswersMap.put(currentQuestionIndex, answer);
                    });

                    // adds the radiobutton
                    answersContainer.getChildren().add(radioButton);
                }




                if (selectedAnswersMap.containsKey(currentQuestionIndex)) {
                    Answer selectedAnswer = selectedAnswersMap.get(currentQuestionIndex);
                }

            } else {

            }


        } else {

            // end of test, not needed buttons not visible
            answersContainer.getChildren().clear();
            questionLabel.setText("");
            endOfTestLabel.setText("End of the test");
            endOfTestLabel.setStyle("-fx-font-size: 40;");
            nextButton.setVisible(false);
            backButton.setVisible(false);
            selectedAnswersMap = null;
        }
    }



    @FXML
    private void handleSubmit() throws IOException {
        // iterate over the selectedAnswersMap entries and add them to the userAnswersMap
        if (selectedAnswersMap == null) {
            selectedAnswersMap = new HashMap<>();
        }

        // iterate through the userAnswersMap and update selectedAnswersMap
        for (Map.Entry<Integer, Answer> entry : userAnswersMap.entrySet()) {
            int index = entry.getKey();
            Answer answer = entry.getValue();
            selectedAnswersMap.put(index, answer);
        }

        // score for testhistory
        int score = calculateScore();
        saveTestHistory(score);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowTestResult.fxml"));
        Parent parent = loader.load();

        ShowTestResultController showTestResultController = loader.getController();

        // pass the current TestController instance for result
        showTestResultController.setTestController(this);

        Scene showTestResultScene = new Scene(parent);
        showTestResultScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) returnToTestSelectionButton.getScene().getWindow();

        stage.setScene(showTestResultScene);

        // we set the controller to null to ensure a new instance is created next time
        loader.setController(null);
    }

    @FXML
    void nextQuestion() {
        if (selectedAnswersMap.containsKey(currentQuestionIndex)) {
            // shows previous answer based on previous question index
            Answer previousAnswer = userAnswersMap.get(currentQuestionIndex);

            // if there was a previous answer for the current question, remove it
            if (previousAnswer != null) {
                userAnswersMap.remove(currentQuestionIndex);
            }

            // add the selected answer to the userAnswersMap
            userAnswersMap.put(currentQuestionIndex, selectedAnswersMap.get(currentQuestionIndex));
        }

        // go to the next question
        currentQuestionIndex++;
        loadQuestionAndAnswers();
    }

    @FXML
    void questionBefore() {
        if (currentQuestionIndex > 0) {
            // goes back and nullifies previous answer
            // update selected answer when going back to the previous question
            selectedAnswersMap.put(currentQuestionIndex, null);

            // remove the current answer from userAnswersMap if it exists
            userAnswersMap.remove(currentQuestionIndex);

            currentQuestionIndex--;
            loadQuestionAndAnswers();
        } else {
            // case where there are no previous questions
            System.out.println("No previous question available");
        }
    }

    private int calculateScore() {
        if (userAnswersMap == null) {
            return 0;
        }

        // counts correct answers and saves them
        long correctAnswersCount = userAnswersMap.values().stream().filter(Answer::getIsCorrect).count();

        // calculate and return the score
        return (int) correctAnswersCount;
    }

    private void saveTestHistory(int score) {
        // create a TestHistory object and save it using the TestHistoryDao
        TestHistory testHistory = new TestHistory();
        testHistory.setScore(score);
        testHistory.setTest(testDao.getTestById(testId));
        // set the user
        Optional<User> currentUser = userDao.getCurrentUser();
        testHistory.setUser(currentUser.get());

        testHistoryDao.saveTestHistory(testHistory);
    }

    // return to test selection
        @FXML
    void returnToTestSelection() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/TestSelection.fxml"));
        Parent parent = loader.load();
        Scene testSelectionScene = new Scene(parent);
        testSelectionScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) returnToTestSelectionButton.getScene().getWindow();
        stage.setScene(testSelectionScene);
    }
}
