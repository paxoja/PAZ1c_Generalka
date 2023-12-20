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

    private Test currentTest; // here we store which test we are taking

    //
    @FXML
    private void handleSubmit() throws IOException {
        if (isDataValid()) {
            // adds question to List testQuestions
            TestQuestion testQuestion = createTestQuestion();
            testQuestions.add(testQuestion);
            // field which we write question into resets
            clearFields();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/generalka.fxml"));
        Parent parent = loader.load();
        Scene generalkaScene = new Scene(parent);
        generalkaScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
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

    // window cant be empty, nor the answers cant be empty
    private boolean isDataValid() {
        return !questionTextField.getText().isEmpty() && !answersTextField.getText().isEmpty();
    }

    private TestQuestion createTestQuestion() {

        // questions takes text fields, sets which test it belongs to
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestion(questionTextField.getText());
        testQuestion.setTest(currentTest);

        // saves question using Dao
        testQuestionDao.saveTestQuestion(testQuestion);

        // answers split into multiple strings and is saved into answers List
        String[] answerOptions = answersTextField.getText().split("\\s*,\\s*");
        List<Answer> answers = new ArrayList<>();

        // cycle - goes through answersOptions string pool, saves answers
        for (String option : answerOptions) {
            Answer answer = new Answer();
            answer.setAnswer(option);

            // sets which answer is true
            if (option.equalsIgnoreCase(correctAnswerTextField.getText())) {
                answer.setIsCorrect(true);
            } else {
                answer.setIsCorrect(false);
            }

            answer.setTestQuestion(testQuestion);
            answers.add(answer);

            // saving answers to database table with answers
            answerDao.saveAnswer(answer);
        }

        // saves to testQuestions table which we use in test
        testQuestion.setAnswers(answers);


        return testQuestion;
    }

    private void clearFields() {
        questionTextField.clear();
        answersTextField.clear();
        correctAnswerTextField.clear();
    }

    // setting which test we are editing
    public void setTest(Test test) {
        this.currentTest = test;
    }

    @FXML
    void returnToTestAttributes() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TestAttributes.fxml"));
        Parent parent = loader.load();
        Scene testAttributesScene = new Scene(parent);
        testAttributesScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) returnToTestAttributesButton.getScene().getWindow();
        stage.setScene(testAttributesScene);
    }
}