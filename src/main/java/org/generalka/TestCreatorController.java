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

    private Test currentTest; // tu storujeme test aky robime

    @FXML
    private void handleSubmit() throws IOException {
        if (isDataValid()) {
            TestQuestion testQuestion = createTestQuestion();
            testQuestions.add(testQuestion);
            clearFields();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/generalka.fxml"));
        Parent parent = loader.load();
        Scene generalkaScene = new Scene(parent);
        Stage stage = (Stage) returnToTestAttributesButton.getScene().getWindow();
        stage.setScene(generalkaScene);
    }

    @FXML
    private void addNextQuestion() {
        if (isDataValid()) {
            TestQuestion testQuestion = createTestQuestion();
            testQuestions.add(testQuestion);
            clearFields();
        }
    }

    private boolean isDataValid() {
        return !questionTextField.getText().isEmpty() && !answersTextField.getText().isEmpty();
    }

    private TestQuestion createTestQuestion() {
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestion(questionTextField.getText());
        testQuestion.setTest(currentTest);

        testQuestionDao.saveTestQuestion(testQuestion);

        String[] answerOptions = answersTextField.getText().split("\\s*,\\s*");
        List<Answer> answers = new ArrayList<>();

        for (String option : answerOptions) {
            Answer answer = new Answer();
            answer.setAnswer(option);

            if (option.equalsIgnoreCase(correctAnswerTextField.getText())) {
                answer.setIsCorrect(true);
            } else {
                answer.setIsCorrect(false);
            }

            answer.setTestQuestion(testQuestion);
            answers.add(answer);

            // Save the answer to the database
            answerDao.saveAnswer(answer);
        }

        testQuestion.setAnswers(answers);


        return testQuestion;
    }

    private void clearFields() {
        questionTextField.clear();
        answersTextField.clear();
        correctAnswerTextField.clear();
    }

    // setneme aky test editujeme TestAttributesController
    public void setTest(Test test) {
        this.currentTest = test;
    }

    @FXML
    void returnToTestAttributes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TestAttributes.fxml"));
        Parent parent = loader.load();
        Scene testAttributesScene = new Scene(parent);
        Stage stage = (Stage) returnToTestAttributesButton.getScene().getWindow();
        stage.setScene(testAttributesScene);
    }
}