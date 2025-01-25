package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.*;

public class PlaytimeStatisticsController {
    @FXML private Label totalPlaytimeLabel;
    @FXML private Label averagePlaytimeLabel;

    private final String statsFile = "playtime_statistics.properties";
    private int totalPlaytime = 0;
    private int sessionCount = 0;

    @FXML
    public void initialize() {
        loadStatistics();
        updateLabels();
    }

    private void loadStatistics() {
        try (BufferedReader reader = new BufferedReader(new FileReader(statsFile))) {
            totalPlaytime = Integer.parseInt(reader.readLine());
            sessionCount = Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            totalPlaytime = 0;
            sessionCount = 0;
        }
    }

    private void updateLabels() {
        totalPlaytimeLabel.setText("Total Playtime: " + totalPlaytime + " min");
        int averagePlaytime = sessionCount == 0 ? 0 : totalPlaytime / sessionCount;
        averagePlaytimeLabel.setText("Average Playtime: " + averagePlaytime + " min");
    }

    @FXML
    private void resetPlaytime() throws IOException {
        totalPlaytime = 0;
        sessionCount = 0;
        updateLabels();
        saveStatistics();
    }

    private void saveStatistics() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(statsFile))) {
            writer.write(totalPlaytime + "\n" + sessionCount);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/parental_controls.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
