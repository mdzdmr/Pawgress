package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class WarningController {

    @FXML
    private void handleAcknowledgeButtonAction(ActionEvent event) {
        // Close the dialog
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleYesButtonAction(ActionEvent event) {
        try {
            // Load the main menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/main_menu.fxml"));
            Parent mainMenuRoot = loader.load();

            // Get the current stage and set the scene to the main menu
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(mainMenuRoot));
            currentStage.setTitle("Main Menu");
        } catch (IOException e) {
            System.err.println("Error loading main menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNoButtonAction(ActionEvent event) {
        // Close the warning dialog and return to gameplay
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
