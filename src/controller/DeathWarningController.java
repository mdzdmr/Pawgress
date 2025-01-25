package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class DeathWarningController {

    @FXML
    private void handleGoToMainMenuButtonAction(ActionEvent event) {
        try {
            // Load the main menu FXML first
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/main_menu.fxml"));
            Parent mainMenuRoot = loader.load();

            // Create a new stage for the main menu
            Stage mainMenuStage = new Stage();
            mainMenuStage.setScene(new Scene(mainMenuRoot));
            mainMenuStage.setTitle("Main Menu");

            // Show the main menu window
            mainMenuStage.show();

            // Now close the current gameplay window (DeathWarning window)
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close(); // Close the gameplay window

        } catch (IOException e) {
            System.err.println("Error loading the main menu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
