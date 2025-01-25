package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MediaPlayerManager {
    private static MediaPlayer mediaPlayer;

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer player) {
        mediaPlayer = player;
    }

    public static void initializeMediaPlayer(String musicFilePath) {
        if (mediaPlayer == null) {
            Media media = new Media(musicFilePath);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        }
    }
}
