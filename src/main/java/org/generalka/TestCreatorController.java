package org.generalka;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.generalka.storage.Answer;
import org.generalka.storage.AnswerDao;
import org.generalka.storage.DaoFactory;
import org.generalka.storage.Test;
import org.generalka.storage.TestQuestion;
import org.generalka.storage.TestQuestionDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestCreatorController {

    @FXML
    private Button returnToTestAttributesButton;

    @FXML
    private VBox questionsVBox;

    private TestQuestionDao testQuestionDao = DaoFactory.INSTANCE.getTestQuestionDao();
    private AnswerDao answerDao = DaoFactory.INSTANCE.getAnswerDao();

    private Test currentTest; // here we store which test we are taking

    // Define a ToggleGroup for the radio buttons
    private ToggleGroup toggleGroup = new ToggleGroup();

    @FXML
    private void handleSubmit() throws IOException {
        // Iterate through all the children of questionsVBox
        for (Node node : questionsVBox.getChildren()) {
            if (node instanceof VBox) {
                VBox questionBox = (VBox) node;
                HBox questionRow = (HBox) questionBox.getChildren().get(0); // First child is the question row
                TextField questionField = (TextField) questionRow.getChildren().get(1); // Second child is the question field

                // Retrieve the HBoxes containing the choices
                List<HBox> choiceRows = new ArrayList<>();
                for (int i = 1; i < questionBox.getChildren().size(); i++) {
                    Node childNode = questionBox.getChildren().get(i);
                    if (childNode instanceof HBox) {
                        choiceRows.add((HBox) childNode);
                    }
                }

                // Extract the text from the choice text fields
                List<String> answersText = new ArrayList<>();
                for (HBox choiceRow : choiceRows) {
                    TextField choiceField = (TextField) choiceRow.getChildren().get(0);
                    answersText.add(choiceField.getText());
                }

                TestQuestion testQuestion = createTestQuestion(questionField.getText(), choiceRows);
                testQuestionDao.saveTestQuestion(testQuestion);
            }
        }

        // After saving all questions, clear the fields
        clearFields();

        // Navigate back to TestAttributes.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/generalka.fxml"));
        Parent parent = loader.load();
        Scene generalkaScene = new Scene(parent);
        generalkaScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) returnToTestAttributesButton.getScene().getWindow();

        stage.setScene(generalkaScene);
    }


    @FXML
    private void addNextQuestion() {
        // Create a new box for the question and related fields
        VBox questionBox = createQuestionBox();

        // Create a new row for question
        HBox questionRow = createQuestionRow();

        // Add the new rows to the question box
        questionBox.getChildren().add(questionRow);

        // Add the question box to the main VBox
        questionsVBox.getChildren().add(questionBox);

        // Add a button to add choices
        Button addChoiceButton = new Button("Add Choice");
        addChoiceButton.setOnAction(event -> addChoice(questionBox));
        questionBox.getChildren().add(addChoiceButton);

        // Add a separator line
        addSeparator();
    }

    private void addChoice(VBox questionBox) {
        HBox choiceRow = createChoiceRow();
        questionBox.getChildren().add(choiceRow);
    }

    private HBox createChoiceRow() {
        HBox choiceRow = new HBox(10);
        TextField choiceField = new TextField();
        choiceField.setPromptText("Enter Choice");
        RadioButton radioButton = new RadioButton();
        radioButton.setToggleGroup(toggleGroup);
        choiceRow.getChildren().addAll(choiceField, radioButton);
        return choiceRow;
    }

    private void addSeparator() {
        Line separator = new Line();
        separator.setEndX(580); // Adjust width to match the original layout
        separator.setStrokeWidth(1);
        separator.setStyle("-fx-stroke: black;");
        questionsVBox.getChildren().add(separator);
    }

    private VBox createQuestionBox() {
        VBox questionBox = new VBox(10); // Adjust spacing as needed
        questionBox.setStyle("-fx-padding: 10px;");
        return questionBox;
    }

    private HBox createQuestionRow() {
        HBox questionRow = new HBox(10);
        Label questionLabel = new Label("Question:");
        TextField questionField = new TextField();
        questionField.setPromptText("Type your question here");
        questionField.setPrefWidth(400);
        questionRow.getChildren().addAll(questionLabel, questionField);
        return questionRow;
    }



    // Check if the data is valid
    private boolean isDataValid(String question, RadioButton selectedRadioButton) {
        return !question.isEmpty() && selectedRadioButton != null;
    }

    private TestQuestion createTestQuestion(String questionText, List<HBox> choiceRows) {
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestion(questionText);
        testQuestion.setTest(currentTest);
        testQuestionDao.saveTestQuestion(testQuestion);

        for (HBox choiceRow : choiceRows) {
            TextField choiceField = (TextField) choiceRow.getChildren().get(0);
            String choiceText = choiceField.getText();

            RadioButton radioButton = (RadioButton) choiceRow.getChildren().get(1);
            boolean isCorrect = radioButton.isSelected();

            Answer answer = new Answer();
            answer.setAnswer(choiceText);
            answer.setIsCorrect(isCorrect);
            answer.setTestQuestion(testQuestion);
            answerDao.saveAnswer(answer);
        }

        return testQuestion;
    }

    // Clear all fields after submitting
    private void clearFields() {
        questionsVBox.getChildren().clear();
        toggleGroup.getToggles().clear(); // Clear toggle group
    }

    // Navigate back to TestAttributes.fxml
    @FXML
    void returnToTestAttributes() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TestAttributes.fxml"));
        Parent parent = loader.load();
        Scene testAttributesScene = new Scene(parent);
        testAttributesScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) returnToTestAttributesButton.getScene().getWindow();
        stage.setScene(testAttributesScene);
    }

    // Set which test we are editing
    public void setTest(Test test) {
        this.currentTest = test;
    }
}
