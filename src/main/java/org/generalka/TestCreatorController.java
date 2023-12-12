package org.generalka;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.generalka.storage.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class TestCreatorController {

    @FXML
    private TextField answersTextField;

    @FXML
    private TextField correctAnswerTextField;  // Add this field

    @FXML
    private VBox checkBoxContainer;

    @FXML
    private TextField questionTextField;

    @FXML
    private Button returnToTestAttributesButton;

    private TestQuestionDao testQuestionDao = DaoFactory.INSTANCE.getTestQuestionDao();
    private AnswerDao answerDao = DaoFactory.INSTANCE.getAnswerDao();


    @FXML
    private void handleSubmit() {
        // Save question and answers to the database
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestion(questionTextField.getText());

        testQuestionDao.saveTestQuestion(testQuestion);

        // Split answers and create Answer objects
        String[] answerOptions = answersTextField.getText().split(",\\s*");
        List<Answer> answers = new ArrayList<>();

        for (String option : answerOptions) {
            Answer answer = new Answer();
            answer.setAnswer(option);

            // Check if the current answer is marked as correct by the user
            if (option.equalsIgnoreCase(correctAnswerTextField.getText())) {
                answer.setCorrect(true);
            } else {
                answer.setCorrect(false);
            }

            answerDao.saveAnswer(answer);

            answers.add(answer);
        }

        testQuestion.setAnswers(answers);

        // Clear previous checkboxes
        checkBoxContainer.getChildren().clear();

        // Create new checkboxes based on user input
        for (Answer answer : answers) {
            CheckBox checkBox = new CheckBox(answer.getAnswer());
            checkBoxContainer.getChildren().add(checkBox);
        }
    }

    @FXML
    void returnToTestAttributes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/TestAttributes.fxml"));
        Parent parent = loader.load();
        Scene testAttributesScene = new Scene(parent);
        Stage stage = (Stage) returnToTestAttributesButton.getScene().getWindow();
        stage.setScene(testAttributesScene);
    }
}