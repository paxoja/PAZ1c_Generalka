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

    private List<String> questions;
    private List<List<String>> answerOptions; // zoznam odpovedi

    private int currentQuestionIndex = 0;

    @FXML
    public void initialize() {
        loadQuestionAndAnswers();
    }

    private void loadQuestionAndAnswers() {
        if (currentQuestionIndex < questions.size()) {
            // naloadujeme otazku
            questionLabel.setText(questions.get(currentQuestionIndex));

            // vymazu sa predosle answer options
            answersContainer.getChildren().clear();

            // naloaduju sa answer options
            for (String option : answerOptions.get(currentQuestionIndex)) {
                CheckBox checkBox = new CheckBox(option);
                answersContainer.getChildren().add(checkBox);
            }
        } else {
            // uplny konec testu, ked uz nebudu ine otazky
            questionLabel.setText("End of the test.");
            answersContainer.getChildren().clear();
        }
    }

    @FXML
    private void handleSubmit() {
        // handler pre terajsiu otazku

        // presun na dalsiu otazku
        currentQuestionIndex++;
        loadQuestionAndAnswers();
    }
}

