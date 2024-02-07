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
import org.generalka.storage.EntityNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestEditController {


    @FXML
    private Button returnToTestAttributesButton;

    @FXML
    private VBox questionsVBox;

    private TestQuestionDao testQuestionDao = DaoFactory.INSTANCE.getTestQuestionDao();
    private AnswerDao answerDao = DaoFactory.INSTANCE.getAnswerDao();

    private Test currentTest; // here we store which test we are taking

    private boolean isEditing = false;

    // define a ToggleGroup for the radio buttons
    private ToggleGroup toggleGroup = new ToggleGroup();

    public TestEditController() {
        this.testQuestionDao = DaoFactory.INSTANCE.getTestQuestionDao();
        this.answerDao = DaoFactory.INSTANCE.getAnswerDao();
        loadQuestionsAndAnswers();  // Call the method during initialization

    }

    private void loadQuestionsAndAnswers() {
        // Assuming currentTest is already set in your controller
        if (isEditing && currentTest != null) {
            try {
                // Fetch questions associated with the current test
                List<TestQuestion> testQuestions = testQuestionDao.getTestQuestionsByTestId(currentTest.getId());

                // Iterate through each question and fetch associated answers
                for (TestQuestion question : testQuestions) {
                    List<Answer> answers = answerDao.getAnswersByQuestionId(question.getId());
                    // Now you have the question and its answers to display/edit
                    // You may need to update your UI components accordingly
                    displayQuestionAndAnswers(question, answers);
                }
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
                // Handle exception as needed
            }
        }
    }

    private void displayQuestionAndAnswers(TestQuestion question, List<Answer> answers) {
        // Create a VBox to hold the question and its answers
        VBox questionBox = createQuestionBox(); // Use the method to create the VBox
        questionBox.setSpacing(10); // Set spacing between elements

        // Set the TestQuestion object as user data for the questionBox
        questionBox.setUserData(question);

        // Create a HBox for the question row
        HBox questionRow = createQuestionRow(); // Use the method to create the HBox
        TextField questionField = (TextField) questionRow.getChildren().get(1);
        questionField.setText(question.getQuestion()); // Set the question text
        questionBox.getChildren().add(questionRow); // Add the question HBox to the VBox

        // Create a ToggleGroup for the RadioButtons
        ToggleGroup toggleGroup = new ToggleGroup();

        // Iterate through each answer and create a HBox for it
        for (Answer answer : answers) {
            HBox answerBox = createChoiceRow(toggleGroup); // Use the method to create the HBox
            TextField choiceField = (TextField) answerBox.getChildren().get(0);
            choiceField.setText(answer.getAnswer()); // Set the answer text

            RadioButton radioButton = (RadioButton) answerBox.getChildren().get(1);
            if (answer.getIsCorrect()) {
                radioButton.setSelected(true); // Set the RadioButton as selected if the answer is correct
            }

            // Add an event handler to the RadioButton to update the database when selection changes
            radioButton.setOnAction(event -> updateCorrectAnswer(answer, radioButton.isSelected()));

            // Create a delete choice button
            Button deleteChoiceButton = new Button("X");
            deleteChoiceButton.setOnAction(event -> deleteChoice(answerBox, questionBox)); // Set the action to delete the choice
            answerBox.getChildren().add(deleteChoiceButton); // Add the delete choice button to the answer HBox

            questionBox.getChildren().add(answerBox); // Add the answer HBox to the question VBox
        }


        // Create an "Add Choice" button
        Button addChoiceButton = new Button("Add Choice");
        addChoiceButton.setOnAction(event -> addChoice(questionBox, toggleGroup)); // Set the action to add a choice
        questionBox.getChildren().add(addChoiceButton); // Add the "Add Choice" button to the question VBox

        // Create a "Delete this question" button
        Button deleteButton = new Button("Delete this question");
        deleteButton.setOnAction(event -> deleteQuestion(event)); // when we click on the button we delete the question
        questionBox.getChildren().add(deleteButton);

        // Add the question VBox to the main VBox (questionsVBox)
        questionsVBox.getChildren().add(questionBox);

        // Add separator line between questions
        addSeparator();
    }


    private void updateCorrectAnswer(Answer answer, boolean isCorrect) {
        answer.setIsCorrect(isCorrect);
        try {
            // Update the Answer object in the database
            answerDao.updateAnswer(answer);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
    }




    @FXML
    private void handleSubmit() throws IOException {
        // Check if there are no questions added
        if (questionsVBox.getChildren().isEmpty()) {
            showAlert("Error", "You need to add at least 1 question to save the test.");
            return;
        }

        // Iterate through each question
        for (Node node : questionsVBox.getChildren()) {
            if (node instanceof VBox) {
                VBox questionBox = (VBox) node;
                TestQuestion testQuestion = (TestQuestion) questionBox.getUserData(); // Get the TestQuestion object

                // Get the question text field
                HBox questionRow = (HBox) questionBox.getChildren().get(0);
                TextField questionField = (TextField) questionRow.getChildren().get(1);

                // Update the question text
                testQuestion.setQuestion(questionField.getText());

                // Save or update the TestQuestion object
                try {
                    if (testQuestion.getId() == null) {
                        // This is a new question, so save it to the database
                        testQuestionDao.saveTestQuestion(testQuestion);
                    } else {
                        // This is an existing question, so update it in the database
                        testQuestionDao.updateTestQuestion(testQuestion);

                        // Delete all existing answers for this question before saving the updated ones
                        List<Answer> existingAnswers = answerDao.getAnswersByQuestionId(testQuestion.getId());
                        for (Answer existingAnswer : existingAnswers) {
                            answerDao.deleteAnswer(existingAnswer.getId());
                        }
                    }

                    // Iterate through each choice (answer)
                    for (int i = 1; i < questionBox.getChildren().size(); i++) {
                        Node choiceNode = questionBox.getChildren().get(i);
                        if (choiceNode instanceof HBox) {
                            HBox choiceRow = (HBox) choiceNode;
                            TextField choiceField = (TextField) choiceRow.getChildren().get(0);
                            RadioButton radioButton = (RadioButton) choiceRow.getChildren().get(1);

                            Answer answer = new Answer();
                            answer.setAnswer(choiceField.getText());
                            answer.setIsCorrect(radioButton.isSelected());
                            answer.setTestQuestion(testQuestion);

                            // Save the answer to the database
                            answerDao.saveAnswer(answer);
                        }
                    }
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                    // Handle exception as needed
                }
            }
        }

        // After we save or update all questions and answers, clear the fields
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
        // Create a new TestQuestion object for the new question
        TestQuestion newQuestion = new TestQuestion();
        newQuestion.setTest(currentTest);

        // Create a VBox to hold the new question and its answers
        VBox questionBox = createQuestionBox();
        questionBox.setSpacing(10);

        // Create a HBox for the question row
        HBox questionRow = createQuestionRow();
        TextField questionField = (TextField) questionRow.getChildren().get(1);
        questionField.setPromptText("Type your question here");
        questionBox.getChildren().add(questionRow);

        // Create a ToggleGroup for the RadioButtons
        ToggleGroup toggleGroup = new ToggleGroup();

        // Create a HBox for each answer row
        for (int i = 0; i < 2; i++) { // Add at least two answer rows initially
            HBox answerRow = createChoiceRow(toggleGroup);
            questionBox.getChildren().add(answerRow);
        }

        // Create an "Add Choice" button
        Button addChoiceButton = new Button("Add Choice");
        addChoiceButton.setOnAction(event -> addChoice(questionBox, toggleGroup));
        questionBox.getChildren().add(addChoiceButton);

        // Create a "Delete this question" button
        Button deleteButton = new Button("Delete this question");
        deleteButton.setOnAction(event -> deleteQuestion(event));
        questionBox.getChildren().add(deleteButton);

        // Add the question box to the main VBox (questionsVBox)
        questionsVBox.getChildren().add(questionBox);

        // Add the separator line between questions
        addSeparator();

        // Set the new question as user data for the questionBox
        questionBox.setUserData(newQuestion);
    }


    private void deleteQuestion(ActionEvent event) {
        Button deleteButton = (Button) event.getSource();
        VBox questionBox = (VBox) deleteButton.getParent();
        TestQuestion question = (TestQuestion) questionBox.getUserData();

        if (question != null) {
            // Check if the question has been saved to the database
            if (question.getId() != null) {
                // This is an existing question, so delete it from the database
                try {
                    List<Answer> answers = answerDao.getAnswersByQuestionId(question.getId());
                    for (Answer answer : answers) {
                        answerDao.deleteAnswer(answer.getId());
                    }
                    testQuestionDao.deleteTestQuestion(question.getId());
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                    // Handle exception as needed
                }
            }
        }

        // Remove the question box and separator from the UI
        questionsVBox.getChildren().remove(questionBox);
        if (questionsVBox.getChildren().size() > 0) {
            questionsVBox.getChildren().remove(questionsVBox.getChildren().size() - 1); // Remove the separator
        }
    }




    private void addChoice(VBox questionBox, ToggleGroup questionToggleGroup) {
        HBox choiceRow = createChoiceRow(questionToggleGroup);
        int addButtonIndex = questionBox.getChildren().size() - 2;
        // Create a delete button for each choice
        Button deleteChoiceButton = new Button("X");
        deleteChoiceButton.setOnAction(event -> deleteChoice(choiceRow, questionBox));
        choiceRow.getChildren().add(deleteChoiceButton);

        questionBox.getChildren().add(addButtonIndex, choiceRow);
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
            return;
        }

        // If there are more than 2 choices, update the database
        TestQuestion question = (TestQuestion) questionBox.getUserData();
        TextField choiceField = (TextField) choiceRow.getChildren().get(0);
        String choiceText = choiceField.getText();

        try {
            // Find the answer associated with this choice text
            List<Answer> answers = answerDao.getAnswersByQuestionId(question.getId());
            for (Answer answer : answers) {
                if (answer.getAnswer().equals(choiceText)) {
                    answerDao.deleteAnswer(answer.getId());
                    break;
                }
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
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

        try {
            if (testQuestion.getId() == null) {
                // This is a new question, so save it to the database
                testQuestionDao.saveTestQuestion(testQuestion);
            } else {
                // This is an existing question, so update it in the database
                testQuestionDao.updateTestQuestion(testQuestion);
            }

            // Save or update each answer in the database
            for (HBox choiceRow : choiceRows) {
                TextField choiceField = (TextField) choiceRow.getChildren().get(0);
                String choiceText = choiceField.getText();

                RadioButton radioButton = (RadioButton) choiceRow.getChildren().get(1);
                boolean isCorrect = radioButton.isSelected();

                Answer answer = new Answer();
                answer.setAnswer(choiceText);
                answer.setIsCorrect(isCorrect);
                answer.setTestQuestion(testQuestion);

                // Save or update the answer in the database
                if (answer.getId() == null) {
                    answerDao.saveAnswer(answer);
                } else {
                    answerDao.updateAnswer(answer);
                }
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
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
    private void returnToTestAttributes() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TestAttributes.fxml"));
        Parent parent = loader.load();
        TestAttributesController testAttributesController = loader.getController();
        testAttributesController.setFilterValues(currentTest.getTopic(), currentTest.getYearOfStudy(), currentTest.getSubject(), currentTest.getSemester(), currentTest.getIsWholeSemester(), currentTest);
        Scene testAttributesScene = new Scene(parent);
        testAttributesScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        Stage stage = (Stage) returnToTestAttributesButton.getScene().getWindow();
        stage.setScene(testAttributesScene);
    }

    // set which test we are editing
    public void setTest(Test test) {
        this.currentTest = test;
        if (test != null) {
            isEditing = true;
            loadQuestionsAndAnswers(); // Load questions and answers when editing an existing test
        } else {
            isEditing = false;
        }
    }

}
