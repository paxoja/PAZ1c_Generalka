package org.generalka;

import javafx.event.ActionEvent;
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

    // define a ToggleGroup for the radio buttons
    private ToggleGroup toggleGroup = new ToggleGroup();

    @FXML
    private void handleSubmit() throws IOException {
        // Check if there are no questions added
        if (questionsVBox.getChildren().isEmpty()) {
            showAlert("Error", "You need to add at least 1 question to save the test.");
            return;
        }

        // Check if all questions have at least 2 choices
        boolean allQuestionsHaveChoices = true;
        for (Node node : questionsVBox.getChildren()) {
            if (node instanceof VBox) {
                VBox questionBox = (VBox) node;
                // Count the choice rows
                int choiceRowCount = 0;
                for (Node childNode : questionBox.getChildren()) {
                    if (childNode instanceof HBox) {
                        choiceRowCount++;
                    }
                }
                // Subtract 1 because the first child is the question itself, not a choice
                if (choiceRowCount - 1 < 2) { // Subtract 1 from choiceRowCount
                    allQuestionsHaveChoices = false;
                    break;
                }
            }
        }

        // If not all questions have at least 2 choices, show an error message
        if (!allQuestionsHaveChoices) {
            showAlert("Error", "Each question must have at least 2 choices.");
            return;
        }

        // Check if all questions have a correct answer selected
        boolean allQuestionsHaveCorrectAnswer = true;
        for (Node node : questionsVBox.getChildren()) {
            if (node instanceof VBox) {
                VBox questionBox = (VBox) node;
                // Check if any RadioButton in the ToggleGroup is selected
                boolean questionHasSelectedAnswer = false;
                for (Node childNode : questionBox.getChildren()) {
                    if (childNode instanceof HBox) {
                        HBox choiceRow = (HBox) childNode;
                        for (Node choiceNode : choiceRow.getChildren()) {
                            if (choiceNode instanceof RadioButton) {
                                RadioButton radioButton = (RadioButton) choiceNode;
                                if (radioButton.isSelected()) {
                                    // Correct answer is selected
                                    questionHasSelectedAnswer = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                // If no RadioButton is selected, set the flag to false
                if (!questionHasSelectedAnswer) {
                    allQuestionsHaveCorrectAnswer = false;
                    break;
                }
            }
        }

        // If not all questions have a correct answer selected, show an error message
        if (!allQuestionsHaveCorrectAnswer) {
            showAlert("Error", "Each question must have a correct answer selected.");
            return;
        }

        // If there are no errors, proceed to save the test questions
        for (Node node : questionsVBox.getChildren()) {
            if (node instanceof VBox) {
                VBox questionBox = (VBox) node;
                HBox questionRow = (HBox) questionBox.getChildren().get(0);
                TextField questionField = (TextField) questionRow.getChildren().get(1);
                List<HBox> choiceRows = new ArrayList<>();
                for (int i = 1; i < questionBox.getChildren().size(); i++) {
                    Node childNode = questionBox.getChildren().get(i);
                    if (childNode instanceof HBox) {
                        choiceRows.add((HBox) childNode);
                    }
                }
                List<String> answersText = new ArrayList<>();
                for (HBox choiceRow : choiceRows) {
                    TextField choiceField = (TextField) choiceRow.getChildren().get(0);
                    answersText.add(choiceField.getText());
                }
                TestQuestion testQuestion = createTestQuestion(questionField.getText(), choiceRows);
                testQuestionDao.saveTestQuestion(testQuestion);
            }
        }

        // After we save all questions, clear the fields
        clearFields();

        // Navigate back to the main screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/generalka.fxml"));
        Parent parent = loader.load();
        Scene generalkaScene = new Scene(parent);
        generalkaScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) returnToTestAttributesButton.getScene().getWindow();
        stage.setScene(generalkaScene);
    }



    // method to show us an alert dialog
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    private void addNextQuestion() {
        // we create a new box for the question and related fields
        VBox questionBox = createQuestionBox();

        // we create a new row for question
        HBox questionRow = createQuestionRow();

        // we add the new rows to the question box
        questionBox.getChildren().add(questionRow);

        // adding the "Delete this question" button
        Button deleteButton = new Button("Delete this question");
        deleteButton.setOnAction(event -> deleteQuestion(event)); // when we click on the button we delete the question
        questionBox.getChildren().add(deleteButton);

        // add the question box to the main VBox
        questionsVBox.getChildren().add(questionBox);

        // for each question there is unique togglegroup with radiobuttons
        ToggleGroup questionToggleGroup = new ToggleGroup();

        // button to add choices (answers)
        Button addChoiceButton = new Button("Add Choice");
        addChoiceButton.setOnAction(event -> addChoice(questionBox, questionToggleGroup));
        questionBox.getChildren().add(addChoiceButton);

        // separator line between questions
        addSeparator();
    }

    @FXML
    private void deleteQuestion(ActionEvent event) {
        // we get the button we clicked
        Button deleteButton = (Button) event.getSource();

        // we get the parent vbox of the button
        VBox questionBox = (VBox) deleteButton.getParent();

        // remove the vbox from the questionsVBox
        questionsVBox.getChildren().remove(questionBox);

        // detail: removing the separator if it's the last question
        if (questionsVBox.getChildren().isEmpty()) {
            questionsVBox.getChildren().remove(questionsVBox.getChildren().size() - 1);
        }
    }

    private void addChoice(VBox questionBox, ToggleGroup questionToggleGroup) {
        HBox choiceRow = createChoiceRow(questionToggleGroup);

        // Create a delete button for each choice
        Button deleteChoiceButton = new Button("X");
        deleteChoiceButton.setOnAction(event -> deleteChoice(choiceRow, questionBox));
        choiceRow.getChildren().add(deleteChoiceButton);

        questionBox.getChildren().add(choiceRow);
    }

    private void deleteChoice(HBox choiceRow, VBox questionBox) {
        // Remove the choice row from the question box
        questionBox.getChildren().remove(choiceRow);

        // Check if there are at least 2 choices remaining
        int remainingChoices = 0;
        for (Node node : questionBox.getChildren()) {
            if (node instanceof HBox) {
                remainingChoices++;
            }
        }
        if (remainingChoices < 2) {
            showAlert("Error", "Each question must have at least 2 choices.");
        }
    }


    private HBox createChoiceRow(ToggleGroup questionToggleGroup) { // Accept ToggleGroup as parameter
        HBox choiceRow = new HBox(10);
        TextField choiceField = new TextField();
        choiceField.setPromptText("Enter Choice");
        RadioButton radioButton = new RadioButton();
        radioButton.setToggleGroup(questionToggleGroup); // radiobuttons for the choice

        // black border for radiobuttons
        radioButton.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 50%;");

        choiceRow.getChildren().addAll(choiceField, radioButton);
        return choiceRow;
    }


    private void addSeparator() {
        // separator line between questions
        Line separator = new Line();
        separator.setEndX(580);
        separator.setStrokeWidth(1);
        separator.setStyle("-fx-stroke: black;");
        questionsVBox.getChildren().add(separator);
    }

    private VBox createQuestionBox() {
        VBox questionBox = new VBox(10);
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

    // clear all fields after submitting
    private void clearFields() {
        questionsVBox.getChildren().clear();
        toggleGroup.getToggles().clear();
    }


    // back to TestAttributes after pressing return button
    @FXML
    void returnToTestAttributes() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TestAttributes.fxml"));
        Parent parent = loader.load();
        Scene testAttributesScene = new Scene(parent);
        testAttributesScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) returnToTestAttributesButton.getScene().getWindow();
        stage.setScene(testAttributesScene);
    }

    // set which test we are editing
    public void setTest(Test test) {
        this.currentTest = test;
    }
}
