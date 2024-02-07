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

public class TestCreatorController {


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

    public TestCreatorController() {
        this.testQuestionDao = DaoFactory.INSTANCE.getTestQuestionDao();
        this.answerDao = DaoFactory.INSTANCE.getAnswerDao();
        loadQuestionsAndAnswers();
    }

    private void loadQuestionsAndAnswers() {
        if (isEditing && currentTest != null) {
            try {
                // fetch questions associated with the current test
                List<TestQuestion> testQuestions = testQuestionDao.getTestQuestionsByTestId(currentTest.getId());

                // iterate through each question and fetch associated answers
                for (TestQuestion question : testQuestions) {
                    List<Answer> answers = answerDao.getAnswersByQuestionId(question.getId());
                    // now you have the question and its answers to display/edit
                    // you may need to update your UI components accordingly
                    displayQuestionAndAnswers(question, answers);
                }
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayQuestionAndAnswers(TestQuestion question, List<Answer> answers) {
        // create a VBox to hold the question and its answers
        VBox questionBox = createQuestionBox(); // use the method to create the VBox
        questionBox.setSpacing(10);

        // set the TestQuestion object as user data for the questionBox
        questionBox.setUserData(question);

        // create a HBox for the question row
        HBox questionRow = createQuestionRow(); // use the method to create the HBox
        TextField questionField = (TextField) questionRow.getChildren().get(1);
        questionField.setText(question.getQuestion()); // set the question text
        questionBox.getChildren().add(questionRow); // add the question HBox to the VBox

        // create a ToggleGroup for the RadioButtons
        ToggleGroup toggleGroup = new ToggleGroup();

        // iterate through each answer and create a HBox for it
        for (Answer answer : answers) {
            HBox answerBox = createChoiceRow(toggleGroup); // use the method to create the HBox
            TextField choiceField = (TextField) answerBox.getChildren().get(0);
            choiceField.setText(answer.getAnswer()); // set the answer text

            RadioButton radioButton = (RadioButton) answerBox.getChildren().get(1);
            if (answer.getIsCorrect()) {
                radioButton.setSelected(true); // set the RadioButton as selected if the answer is correct
            }

            // add an event handler to the RadioButton to update the database when selection changes
            radioButton.setOnAction(event -> updateCorrectAnswer(answer, radioButton.isSelected()));

            // Create a delete choice button
            Button deleteChoiceButton = new Button("X");
            deleteChoiceButton.setOnAction(event -> deleteChoice(answerBox, questionBox)); // Sst the action to delete the choice
            answerBox.getChildren().add(deleteChoiceButton); // add the delete choice button to the answer HBox

            questionBox.getChildren().add(answerBox); // add the answer HBox to the question VBox
        }


        // create an "Add Choice" button
        Button addChoiceButton = new Button("Add Choice");
        addChoiceButton.setOnAction(event -> addChoice(questionBox, toggleGroup)); // Set the action to add a choice
        questionBox.getChildren().add(addChoiceButton); // Add the "Add Choice" button to the question VBox

        // create a "Delete this question" button
        Button deleteButton = new Button("Delete this question");
        deleteButton.setOnAction(event -> deleteQuestion(event)); // when we click on the button we delete the question
        questionBox.getChildren().add(deleteButton);

        // add the question VBox to the main VBox (questionsVBox)
        questionsVBox.getChildren().add(questionBox);

        // add separator line between questions
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
        // check if there are no questions added
        if (questionsVBox.getChildren().isEmpty()) {
            showAlert("Error", "You need to add at least 1 question to save the test.");
            return;
        }

        // accumulate errors in a StringBuilder
        StringBuilder errorBuilder = new StringBuilder();

        // validate each question and choice
        for (Node node : questionsVBox.getChildren()) {
            if (node instanceof VBox) {
                VBox questionBox = (VBox) node;
                HBox questionRow = (HBox) questionBox.getChildren().get(0);
                TextField questionField = (TextField) questionRow.getChildren().get(1);

                // check if question field is empty
                if (questionField.getText().trim().isEmpty()) {
                    errorBuilder.append("Question cannot be left blank.\n");
                }

                // check each choice for this question
                for (int i = 1; i < questionBox.getChildren().size(); i++) {
                    Node childNode = questionBox.getChildren().get(i);
                    if (childNode instanceof HBox) {
                        HBox choiceRow = (HBox) childNode;
                        TextField choiceField = (TextField) choiceRow.getChildren().get(0);

                        // check if choice field is empty
                        if (choiceField.getText().trim().isEmpty()) {
                            errorBuilder.append("Choice cannot be left blank.\n");
                        }
                    }
                }
            }
        }

        // check if any errors occurred
        String errors = errorBuilder.toString().trim();
        if (!errors.isEmpty()) {
            showAlert("Error", errors);
            return;
        }

        // Check if all questions have at least 2 choices
        boolean allQuestionsHaveChoices = true;
        for (Node node : questionsVBox.getChildren()) {
            if (node instanceof VBox) {
                VBox questionBox = (VBox) node;
                // count the choice rows
                int choiceRowCount = 0;
                for (Node childNode : questionBox.getChildren()) {
                    if (childNode instanceof HBox) {
                        choiceRowCount++;
                    }
                }
                // subtract 1 because the first child is the question itself, not a choice
                if (choiceRowCount - 1 < 2) { // Subtract 1 from choiceRowCount
                    allQuestionsHaveChoices = false;
                    break;
                }
            }
        }

        // if not all questions have at least 2 choices, show an error message
        if (!allQuestionsHaveChoices) {
            showAlert("Error", "Each question must have at least 2 choices.");
            return;
        }

        // check if all questions have a correct answer selected
        boolean allQuestionsHaveCorrectAnswer = true;
        for (Node node : questionsVBox.getChildren()) {
            if (node instanceof VBox) {
                VBox questionBox = (VBox) node;
                // check if any RadioButton in the ToggleGroup is selected
                boolean questionHasSelectedAnswer = false;
                for (Node childNode : questionBox.getChildren()) {
                    if (childNode instanceof HBox) {
                        HBox choiceRow = (HBox) childNode;
                        for (Node choiceNode : choiceRow.getChildren()) {
                            if (choiceNode instanceof RadioButton) {
                                RadioButton radioButton = (RadioButton) choiceNode;
                                if (radioButton.isSelected()) {
                                    // correct answer is selected
                                    questionHasSelectedAnswer = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                // if no RadioButton is selected, set the flag to false
                if (!questionHasSelectedAnswer) {
                    allQuestionsHaveCorrectAnswer = false;
                    break;
                }
            }
        }

        // if not all questions have a correct answer selected, show an error message
        if (!allQuestionsHaveCorrectAnswer) {
            showAlert("Error", "Each question must have a correct answer selected.");
            return;
        }

        // if there are no errors, proceed to save the test questions
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

        // after we save all questions, clear the fields
        clearFields();

        // navigate back to the main screen
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

    private void deleteQuestion(ActionEvent event) {
        Button deleteButton = (Button) event.getSource();
        VBox questionBox = (VBox) deleteButton.getParent();
        TestQuestion question = (TestQuestion) questionBox.getUserData();

        if (question != null) {
            // check if the question has been saved to the database
            if (question.getId() != null) {
                // this is an existing question, so delete it from the database
                try {
                    List<Answer> answers = answerDao.getAnswersByQuestionId(question.getId());
                    for (Answer answer : answers) {
                        answerDao.deleteAnswer(answer.getId());
                    }
                    testQuestionDao.deleteTestQuestion(question.getId());
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                // if its just a question in UI not saved yet we just delete it from UI
                questionsVBox.getChildren().remove(questionBox);
            }
        }

        // remove the question box from the UI
        questionsVBox.getChildren().remove(questionBox);
        if (questionsVBox.getChildren().isEmpty()) {
            questionsVBox.getChildren().remove(questionsVBox.getChildren().size() - 1);
        }
    }



    private void addChoice(VBox questionBox, ToggleGroup questionToggleGroup) {
        HBox choiceRow = createChoiceRow(questionToggleGroup);
        int addButtonIndex = questionBox.getChildren().size() - 2;
        // create a delete button for each choice
        Button deleteChoiceButton = new Button("X");
        deleteChoiceButton.setOnAction(event -> deleteChoice(choiceRow, questionBox));
        choiceRow.getChildren().add(deleteChoiceButton);

        questionBox.getChildren().add(addButtonIndex, choiceRow);
    }




    private void deleteChoice(HBox choiceRow, VBox questionBox) {
        // remove the choice row from the question box
        questionBox.getChildren().remove(choiceRow);

        // check if there are at least 2 choices remaining
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

        // if there are more than 2 choices, update the database
        TestQuestion question = (TestQuestion) questionBox.getUserData();
        TextField choiceField = (TextField) choiceRow.getChildren().get(0);
        String choiceText = choiceField.getText();

        try {
            // find the answer associated with this choice text
            List<Answer> answers = answerDao.getAnswersByQuestionId(question.getId());
            for (Answer answer : answers) {
                if (answer.getAnswer().equals(choiceText)) {
                    answerDao.deleteAnswer(answer.getId());
                    break;
                }
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }



    private HBox createChoiceRow(ToggleGroup questionToggleGroup) {
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
                // if new question save it to the database
                testQuestionDao.saveTestQuestion(testQuestion);
            } else {
                // if existing update it
                testQuestionDao.updateTestQuestion(testQuestion);
            }

            // save or update each answer in the database
            for (HBox choiceRow : choiceRows) {
                TextField choiceField = (TextField) choiceRow.getChildren().get(0);
                String choiceText = choiceField.getText();

                RadioButton radioButton = (RadioButton) choiceRow.getChildren().get(1);
                boolean isCorrect = radioButton.isSelected();

                Answer answer = new Answer();
                answer.setAnswer(choiceText);
                answer.setIsCorrect(isCorrect);
                answer.setTestQuestion(testQuestion);

                // save or update the answer in the database
                if (answer.getId() == null) {
                    answerDao.saveAnswer(answer);
                } else {
                    answerDao.updateAnswer(answer);
                }
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
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
            loadQuestionsAndAnswers(); // load questions and answers when editing an existing test
        } else {
            isEditing = false;
        }
    }

}
