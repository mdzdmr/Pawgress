package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class PasswordController {
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessage;

    private final String PASSWORD = "parent1234";

    @FXML
    private void handleEnterButton(ActionEvent event) throws IOException {
        if (passwordField.getText().equals(PASSWORD)) {
            // Navigate to Parental Controls screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/parental_controls.fxml"));
            Parent controlsRoot = loader.load();

            Scene controlsScene = new Scene(controlsRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(controlsScene);
        } else {
            errorMessage.setVisible(true);
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) throws IOException {
        // Navigate back to the main menu
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/main_menu.fxml"));
        Parent mainMenuRoot = loader.load();

        Scene mainMenuScene = new Scene(mainMenuRoot);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);
    }
}
