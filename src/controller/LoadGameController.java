package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

import model.Pet;

public class LoadGameController {

    @FXML
    private Label slot1Name;
    @FXML
    private ImageView slot1Image;
    @FXML
    private Label slot1Level;

    @FXML
    private Label slot2Name;
    @FXML
    private ImageView slot2Image;
    @FXML
    private Label slot2Level;

    @FXML
    private Label slot3Name;
    @FXML
    private ImageView slot3Image;
    @FXML
    private Label slot3Level;

    @FXML
    private Button loadButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button backButton;

    @FXML
    private HBox slot1Container;
    @FXML
    private HBox slot2Container;
    @FXML
    private HBox slot3Container;

    // Updated file names to match what we used in the save logic (save0.txt, save1.txt, save2.txt)
    private final String[] saveFiles = {
            "src/save0.txt",
            "src/save1.txt",
            "src/save2.txt"
    };

    private int selectedSlot = -1; // no slot selected initially

    @FXML
    public void initialize() {
        loadSlotData(1, slot1Name, slot1Level, slot1Image);
        loadSlotData(2, slot2Name, slot2Level, slot2Image);
        loadSlotData(3, slot3Name, slot3Level, slot3Image);
    }

    private void loadSlotData(int slotNumber, Label nameLabel, Label scoreLabel, ImageView imageView) {
        String filePath = saveFiles[slotNumber - 1];
        File file = new File(filePath);
        if (!file.exists()) {
            nameLabel.setText("Empty Slot");
            scoreLabel.setText("");
            imageView.setImage(null);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String petTypeLine = reader.readLine();
            String petNameLine = reader.readLine();
            String scoreLine = reader.readLine();

            if (petTypeLine == null || petNameLine == null || scoreLine == null) {
                nameLabel.setText("Empty Slot");
                scoreLabel.setText("");
                imageView.setImage(null);
                return;
            }

            String petType = petTypeLine.split(": ")[1];
            String petName = petNameLine.split(": ")[1];
            String score = scoreLine.split(": ")[1];

            nameLabel.setText("Pet Name: " + petName);
            scoreLabel.setText("Score: " + score);
            imageView.setImage(getPetImage(petType));

        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            // If the file is invalid or cannot be read properly
            nameLabel.setText("Empty Slot");
            scoreLabel.setText("");
            imageView.setImage(null);
        }
    }

    private Image getPetImage(String petType) {
        String imagePath = "/resources/" + petType.toLowerCase() + ".png";
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream != null) {
            return new Image(imageStream);
        } else {
            System.err.println("Image not found for pet type: " + petType);
            return null;
        }
    }

    @FXML
    private void handleSlot1Selection() {
        selectedSlot = 1;
        highlightSelectedSlot(1);
    }

    @FXML
    private void handleSlot2Selection() {
        selectedSlot = 2;
        highlightSelectedSlot(2);
    }

    @FXML
    private void handleSlot3Selection() {
        selectedSlot = 3;
        highlightSelectedSlot(3);
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        navigateToScene(event, "/resources/main_menu.fxml");
    }

    private void navigateToScene(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        if (selectedSlot == -1) {
            return; // No slot selected
        }
        clearSlotData(selectedSlot);
    }

    private void clearSlotData(int slotNumber) {
        String filePath = saveFiles[slotNumber - 1];
        File file = new File(filePath);
        if (file.exists() && file.delete()) {
            System.out.println("Slot " + slotNumber + " data deleted.");
        }
        resetSlotUI(slotNumber);
    }

    private void resetSlotUI(int slotNumber) {
        switch (slotNumber) {
            case 1 -> {
                slot1Name.setText("Empty Slot");
                slot1Level.setText("");
                slot1Image.setImage(null);
            }
            case 2 -> {
                slot2Name.setText("Empty Slot");
                slot2Level.setText("");
                slot2Image.setImage(null);
            }
            case 3 -> {
                slot3Name.setText("Empty Slot");
                slot3Level.setText("");
                slot3Image.setImage(null);
            }
        }
    }

    @FXML
    private void handleLoadButtonAction(ActionEvent event) throws IOException {
        if (selectedSlot == -1) {
            // No slot selected
            return;
        }
        String filePath = saveFiles[selectedSlot - 1];
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("Selected slot is empty.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String petType = reader.readLine().split(": ")[1];
            String petName = reader.readLine().split(": ")[1];
            int score = Integer.parseInt(reader.readLine().split(": ")[1]);

            // Load the game with the selected pet data
            System.out.println("Loading game with PetType: " + petType + ", PetName: " + petName + ", Score: " + score);
            Pet pet = new Pet(petType, petName, score);

            // Pass this pet to the gameplay scene
            navigateToGameplayScene(event, pet);

        } catch (IOException e) {
            System.err.println("Error loading slot " + selectedSlot + ": " + e.getMessage());
        }
    }

    private void navigateToGameplayScene(ActionEvent event, Pet pet) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gameplay.fxml"));
        Parent root = loader.load();
        GameplayController controller = loader.getController();
        controller.initializePet(pet);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    private void highlightSelectedSlot(int slotNumber) {
        slot1Container.setStyle("");
        slot2Container.setStyle("");
        slot3Container.setStyle("");

        switch (slotNumber) {
            case 1:
                slot1Container.setStyle("-fx-border-color: #42b68c; -fx-border-width: 5;");
                break;
            case 2:
                slot2Container.setStyle("-fx-border-color: #806fd6; -fx-border-width: 5;");
                break;
            case 3:
                slot3Container.setStyle("-fx-border-color: #3380a5; -fx-border-width: 5;");
                break;
        }
    }

}
