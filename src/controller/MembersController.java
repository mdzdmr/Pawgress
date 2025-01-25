package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MembersController {
    @FXML private ImageView infoImage;
    @FXML private Button closeButton;

    @FXML
    public void initialize() {
        // Load the image into the ImageView
        try {
            Image membersImage = new Image(Objects.requireNonNull(getClass().getResource("/resources/members.png")).toExternalForm());
            infoImage.setImage(membersImage);
        } catch (NullPointerException e) {
            System.err.println("Error: Could not load members.jpg");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        // Load the main menu FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/main_menu.fxml"));
        Parent mainMenuRoot = loader.load();

        // Get the current stage and set the scene to the main menu
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(new Scene(mainMenuRoot));
    }
}
