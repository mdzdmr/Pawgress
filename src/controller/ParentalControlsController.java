package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class ParentalControlsController {

    @FXML
    private void handleTimeLimit(ActionEvent event) throws IOException {
        navigateToScene(event, "/resources/time_limit.fxml");
    }

    @FXML
    private void handlePlaytimeStatistics(ActionEvent event) throws IOException {
        navigateToScene(event, "/resources/playtime_statistics.fxml");
    }

    @FXML
    private void handleRevivePet(ActionEvent event) throws IOException {
        navigateToScene(event, "/resources/revive_pet.fxml");
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        navigateToScene(event, "/resources/main_menu.fxml");
    }

    private void navigateToScene(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
