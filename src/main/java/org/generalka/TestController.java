package org.generalka;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.generalka.storage.Answer;
import org.generalka.storage.TestQuestion;

import java.awt.event.ActionEvent;
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
    private int currentQuestionIndex = 0;

    @FXML
    public void initialize() {
        if (testQuestions != null) {
            loadQuestionAndAnswers();
        }
    }

    // setter pre ktory test bereme
    public void setTestId(Long testId) {
        this.testId = testId;

    }

    // setter pre otazky
    public void setTestQuestions(List<TestQuestion> testQuestions) {
        this.testQuestions = testQuestions;
        if (questionLabel != null && answersContainer != null) {
            loadQuestionAndAnswers();
        }
    }

    private void loadQuestionAndAnswers() {
        if (currentQuestionIndex < testQuestions.size()) {
            // nacita otazku
            TestQuestion currentQuestion = testQuestions.get(currentQuestionIndex);
            questionLabel.setText(currentQuestion.getQuestion());

            // pri dalsej odstrani veci zo starej
            answersContainer.getChildren().clear();

            // nacita odpovede moznosti
            for (Answer answer : currentQuestion.getAnswers()) {
                CheckBox checkBox = new CheckBox(answer.getAnswer());
                answersContainer.getChildren().add(checkBox);
            }
        } else {
            // koniec testu
            questionLabel.setText("End of the test.");
            answersContainer.getChildren().clear();
        }
    }

    @FXML
    private void handleSubmit() {
        // check selected answers, save results atd

        // prechod na dalsiu otazku
        currentQuestionIndex++;
        loadQuestionAndAnswers();
    }

    // prechod spat na vyber testov
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