import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.MediaPlayerManager;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initialize the MediaPlayer if not already initialized
        String musicFilePath = Objects.requireNonNull(getClass().getResource("/resources/music.mp3")).toExternalForm();
        MediaPlayerManager.initializeMediaPlayer(musicFilePath);

        // Load the main menu FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/main_menu.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 610, 410);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pawgress");
        primaryStage.show();
    }

    @Override
    public void stop() { if (MediaPlayerManager.getMediaPlayer() != null) { MediaPlayerManager.getMediaPlayer().stop(); } }

    public static void main(String[] args) { launch(args); }

}
