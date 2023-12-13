package org.generalka;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.generalka.storage.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestCreatorController {

    @FXML
    private TextField answersTextField;

    @FXML
    private TextField correctAnswerTextField;

    @FXML
    private TextField questionTextField;

    @FXML
    private Button returnToTestAttributesButton;

    private TestQuestionDao testQuestionDao = DaoFactory.INSTANCE.getTestQuestionDao();
    private AnswerDao answerDao = DaoFactory.INSTANCE.getAnswerDao();

    private List<TestQuestion> testQuestions = new ArrayList<>();

    @FXML
    private void handleSubmit() throws IOException {
        // Save question and answers to the database
        if (isDataValid()) {
            TestQuestion testQuestion = createTestQuestion();
            testQuestions.add(testQuestion);
            // Clear the fields for the next question
            clearFields();
            // Optionally, you can exit the scene or perform other actions here
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/generalka.fxml"));
        Parent parent = loader.load();
        Scene generalkaScene = new Scene(parent);
        Stage stage = (Stage) returnToTestAttributesButton.getScene().getWindow();
        stage.setScene(generalkaScene);
    }

    @FXML
    private void addNextQuestion() {
        // Save question and answers to the database
        if (isDataValid()) {
            TestQuestion testQuestion = createTestQuestion();
            testQuestions.add(testQuestion);
            // Clear the fields for the next question
            clearFields();
        }
    }

    private boolean isDataValid() {
        // Implement validation logic here
        // For example, check if questionTextField and answersTextField are not empty
        return !questionTextField.getText().isEmpty() && !answersTextField.getText().isEmpty();
    }

    private TestQuestion createTestQuestion() {
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestion(questionTextField.getText());

        // Split answers and create Answer objects
        String[] answerOptions = answersTextField.getText().split(",\\s*");
        List<Answer> answers = new ArrayList<>();

        for (String option : answerOptions) {
            Answer answer = new Answer();
            answer.setAnswer(option);

            // Check if the current answer is marked as correct by the user
            if (option.equalsIgnoreCase(correctAnswerTextField.getText())) {
                answer.setIsCorrect(true);
            } else {
                answer.setIsCorrect(false);
            }

            // Set the TestQuestion for the answer
            answer.setTestQuestion(testQuestion);

            answers.add(answer);
        }

        // Set the created answers to the testQuestion
        testQuestion.setAnswers(answers);

        // Save the testQuestion (which includes saving associated answers)
        testQuestionDao.saveTestQuestion(testQuestion);

        return testQuestion;
    }


    private void clearFields() {
        questionTextField.clear();
        answersTextField.clear();
        correctAnswerTextField.clear();
    }

    @FXML
    void returnToTestAttributes(ActionEvent event) throws IOException {
        // Handle any necessary logic before returning to Test Attributes
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/TestAttributes.fxml"));
        Parent parent = loader.load();
        Scene testAttributesScene = new Scene(parent);
        Stage stage = (Stage) returnToTestAttributesButton.getScene().getWindow();
        stage.setScene(testAttributesScene);
    }
}
