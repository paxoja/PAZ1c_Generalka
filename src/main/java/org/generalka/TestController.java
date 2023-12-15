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
import java.util.List;

public class TestController {

    @FXML
    private Label questionLabel;

    @FXML
    private VBox answersContainer;

    private List<TestQuestion> testQuestions;

    @FXML
    private Button returnToTestSelectionButton;

    private Long testId;
    private TestQuestionDao testQuestionDao = DaoFactory.INSTANCE.getTestQuestionDao();
    private AnswerDao answerDao = DaoFactory.INSTANCE.getAnswerDao();
    private int currentQuestionIndex = 0;

    @FXML
    public void initialize() {
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

                    answersContainer.getChildren().add(radioButton);
                }
            } else {
                // Handle the case where answers are null or empty
                // You might want to display a message or take appropriate action
            }
        } else {
            questionLabel.setText("End of the test.");
            answersContainer.getChildren().clear();
        }
    }


    @FXML
    private void handleSubmit() {
        // Check selected answers, save results, etc.

        currentQuestionIndex++;
        loadQuestionAndAnswers();
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
