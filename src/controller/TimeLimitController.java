package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.util.Properties;

public class TimeLimitController {
    @FXML private CheckBox enableLimitations;
    @FXML private ComboBox<String> startTime;
    @FXML private ComboBox<String> endTime;

    private final Properties settings = new Properties();
    private final String settingsFile = "time_limits.properties";

    @FXML
    public void initialize() {
        startTime.getItems().addAll("6 AM", "7 AM", "8 AM", "9 AM");
        endTime.getItems().addAll("6 PM", "7 PM", "8 PM", "9 PM");
        loadSettings();
    }

    private void loadSettings() {
        try (InputStream input = new FileInputStream(settingsFile)) {
            settings.load(input);
            enableLimitations.setSelected(Boolean.parseBoolean(settings.getProperty("enabled")));
            startTime.setValue(settings.getProperty("start"));
            endTime.setValue(settings.getProperty("end"));
        } catch (IOException e) {
            System.err.println("Error loading settings.");
        }
    }

    @FXML
    private void saveSettings() throws IOException {
        try (OutputStream output = new FileOutputStream(settingsFile)) {
            settings.setProperty("enabled", String.valueOf(enableLimitations.isSelected()));
            settings.setProperty("start", startTime.getValue());
            settings.setProperty("end", endTime.getValue());
            settings.store(output, null);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        navigateBack(event);
    }

    private void navigateBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/parental_controls.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
