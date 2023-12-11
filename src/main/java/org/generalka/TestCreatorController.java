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

import java.io.IOException;


public class TestCreatorController {

    @FXML
    private TextField answersTextField;

    @FXML
    private TextField answersTextField1;

    @FXML
    private VBox checkBoxContainer;

    @FXML
    private TextField questionTextField;

    @FXML
    private Button returnToTestAttributesButton;



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
