package org.generalka;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.generalka.storage.Answer;
import org.generalka.storage.TestQuestion;

import java.awt.event.ActionEvent;
import java.util.List;

public class TestController {
    @FXML
    private Label questionLabel;

    @FXML
    private VBox answersContainer;

    private List<TestQuestion> testQuestions;
    private int currentQuestionIndex = 0;

    @FXML
    public void initialize() {
        loadQuestionAndAnswers();
    }

    // Set test questions
    public void setTestQuestions(List<TestQuestion> testQuestions) {
        this.testQuestions = testQuestions;
        loadQuestionAndAnswers();
    }

    private void loadQuestionAndAnswers() {
        if (currentQuestionIndex < testQuestions.size()) {
            // Load question
            TestQuestion currentQuestion = testQuestions.get(currentQuestionIndex);
            questionLabel.setText(currentQuestion.getQuestion());

            // Clear previous answer options
            answersContainer.getChildren().clear();

            // Load answer options
            for (Answer answer : currentQuestion.getAnswers()) {
                CheckBox checkBox = new CheckBox(answer.getAnswer());
                answersContainer.getChildren().add(checkBox);
            }
        } else {
            // End of the test
            questionLabel.setText("End of the test.");
            answersContainer.getChildren().clear();
        }
    }

    @FXML
    private void handleSubmit() {
        // Handle the current question (check selected answers, save results, etc.)

        // Move to the next question
        currentQuestionIndex++;
        loadQuestionAndAnswers();
    }

    @FXML
    void moveToGeneralka(ActionEvent event) {

    }
}
