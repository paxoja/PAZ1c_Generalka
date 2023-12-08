package org.generalka;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class TestController {
    @FXML
    private Label questionLabel;

    @FXML
    private VBox answersContainer;

    private List<String> questions; // Assume you have a list of questions
    private List<List<String>> answerOptions; // Assume you have a list of lists containing answer options

    private int currentQuestionIndex = 0;

    @FXML
    public void initialize() {
        loadQuestionAndAnswers();
    }

    private void loadQuestionAndAnswers() {
        if (currentQuestionIndex < questions.size()) {
            // Load the current question
            questionLabel.setText(questions.get(currentQuestionIndex));

            // Clear previous answer options
            answersContainer.getChildren().clear();

            // Load answer options dynamically
            for (String option : answerOptions.get(currentQuestionIndex)) {
                CheckBox checkBox = new CheckBox(option);
                answersContainer.getChildren().add(checkBox);
            }
        } else {
            // Handle the end of the test (no more questions)
            questionLabel.setText("End of the test.");
            answersContainer.getChildren().clear();
        }
    }

    @FXML
    private void handleSubmit() {
        // Add logic to handle user submission for the current question

        // Move to the next question
        currentQuestionIndex++;
        loadQuestionAndAnswers();
    }
}

