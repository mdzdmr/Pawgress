package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.util.Objects;
import javafx.application.Platform;

public class MainMenuController {

    @FXML private ImageView speakerIcon;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = true;
    private Image speakerOnImage;
    private Image speakerOffImage;
    @FXML private StackPane infoPane;
    @FXML private ImageView infoImage;


    @FXML
    public void initialize() {
        // Load speaker images
        speakerOnImage = new Image(Objects.requireNonNull(getClass().getResource("/resources/speaker_on.png")).toExternalForm());
        speakerOffImage = new Image(Objects.requireNonNull(getClass().getResource("/resources/speaker_off.png")).toExternalForm());

        // Retrieve the MediaPlayer instance
        mediaPlayer = MediaPlayerManager.getMediaPlayer();

        // Check if the MediaPlayer is playing and update the icon state
        if (mediaPlayer != null) {
            setIconState(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING);
        }
    }

    private void setIconState(boolean isPlaying) {
        speakerIcon.setImage(isPlaying ? speakerOnImage : speakerOffImage);
        this.isPlaying = isPlaying; // Update the controller's state
    }

    @FXML
    private void toggleMusic() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                mediaPlayer.pause();
                setIconState(false);
            } else {
                mediaPlayer.play();
                setIconState(true);
            }
        }
    }

    @FXML
    private void handleParent(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/password_screen.fxml"));
        Parent passwordRoot = loader.load();

        Scene passwordScene = new Scene(passwordRoot);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(passwordScene);
        window.show();
    }


    @FXML
    private void loadTutorial(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/tutorial.fxml"));
        Parent tutorialRoot = loader.load();

        Scene tutorialScene = new Scene(tutorialRoot);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tutorialScene);
    }

    @FXML
    private void handleNewGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/pet_selection.fxml"));
        Parent petSelectionRoot = loader.load();

        Scene petSelectionScene = new Scene(petSelectionRoot);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(petSelectionScene);
        stage.show();
    }

    @FXML
    private void showInfo(MouseEvent event) throws IOException {
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/members.fxml"));
        Parent infoRoot = loader.load();

        // Get the current stage (the window)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new scene in the current stage
        currentStage.setScene(new Scene(infoRoot));
    }

    @FXML
    private void loadGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/load_game.fxml"));
        Parent loadGameRoot = loader.load();

        Scene loadGameScene = new Scene(loadGameRoot);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loadGameScene);
        window.show();
    }


    @FXML
    private void handleExitGame(ActionEvent event) {
        // Stop the media player if it's playing
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.stop();
        }

        // Close the current stage (window)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (stage.isShowing()) {
            stage.close();
        }

    }



}
