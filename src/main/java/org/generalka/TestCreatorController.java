package org.generalka;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class TestCreatorController {

    @FXML
    private VBox checkBoxContainer;

    @FXML
    private TextField questionTextField;

    @FXML
    private TextField answersTextField;

    @FXML
    private void handleSubmit() {
        String question = questionTextField.getText();
        String[] answerOptions = answersTextField.getText().split(",\\s*");

        // Clear previous checkboxes
        checkBoxContainer.getChildren().clear();

        // Create new checkboxes based on user input
        for (String option : answerOptions) {
            CheckBox checkBox = new CheckBox(option);
            checkBoxContainer.getChildren().add(checkBox);
        }
    }

}
