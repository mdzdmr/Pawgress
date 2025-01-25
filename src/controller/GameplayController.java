package controller;
import save.SaveManager;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.util.Duration;
import model.Pet;
import model.PetFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.PetState;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import java.io.IOException;
import java.util.Objects;

public class GameplayController {
    @FXML private Label petName;
    @FXML private Label scoreValue;
    @FXML private ProgressBar healthBar;
    @FXML private ProgressBar happinessBar;
    @FXML private ProgressBar hungerBar;
    @FXML private ProgressBar sleepBar;
    @FXML private ImageView petImage;
    @FXML private ImageView backgroundImage;

    @FXML private Button feedButton;
    @FXML private Button giftButton;
    @FXML private Button playButton;
    @FXML private Button vetButton;
    @FXML private Button bedButton;
    @FXML private Button exerciseButton;
    @FXML private InventoryController inventoryController;

    private Pet currentPet;
    private int score = 0;
    private Timeline timeline; // For periodic updates
    private boolean healthWarningShown = false;
    private boolean happinessWarningShown = false;
    private boolean hungerWarningShown = false;
    private boolean sleepWarningShown = false;
    private boolean gameOverDisplayed = false;
    private static GameplayController instance;
    private boolean isVetCooldownActive = false;
    private boolean isPlayCooldownActive = false;
    private boolean isNewGame = true; // Defaults to a new game
    private int loadedFromSlot = -1; // Defaults to not loaded from any slot


    private static final int VET_COOLDOWN_TIME = 45; // 45 seconds cooldown for vet
    private static final int PLAY_COOLDOWN_TIME = 15; // 15 seconds cooldown for play


    @FXML
    public void initialize() {
        instance = this;
        currentPet = PetFactory.createAvailablePets().getFirst(); // Default pet for testing

        petName.setText(currentPet.getType());
        updateBackgroundImage();
        updatePetImage();
        initializeButtonHandlers();

        startStatDecay();

        updateStats();
    }

    public void initializePet(Pet pet) {
        this.currentPet = pet;

        petName.setText(pet.getName());
        updateBackgroundImage();
        updatePetImage();

        startStatDecay();
        updateStats();
    }


    private void startStatDecay() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            currentPet.tick();
            updateStats();
            updatePetImage(); // Update immediately when state changes
            checkPetStatus();
            updateButtonState();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updatePetImage() {
        String imagePath = currentPet.getStateImagePath();
        petImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
    }

    private void updateBackgroundImage() {
        String backgroundPath = "/resources/" + currentPet.getType().toLowerCase() + "_background.png";
        backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(backgroundPath))));
    }

    private void updateStats() {
        healthBar.setProgress(currentPet.getHealth() / 100.0);
        happinessBar.setProgress(currentPet.getHappiness() / 100.0);
        hungerBar.setProgress(currentPet.getFullness() / 100.0);
        sleepBar.setProgress(currentPet.getSleep() / 100.0);
        scoreValue.setText("Score: " + score);

        // Ensure the pet's image updates immediately
        updatePetImage();

        // Update bar colors if values are below 25%
        updateBarColor(healthBar, currentPet.getHealth());
        updateBarColor(happinessBar, currentPet.getHappiness());
        updateBarColor(hungerBar, currentPet.getFullness());
        updateBarColor(sleepBar, currentPet.getSleep());

        // Check and handle stat warnings
        if (!gameOverDisplayed) {
            checkAndDisplayWarnings();
        }
    }

    private void checkAndDisplayWarnings() {
        if (currentPet.getHealth() < 25 && !healthWarningShown) {
            showWarningPage("/resources/stats-health.fxml");
            healthWarningShown = true;
        } else if (currentPet.getHealth() >= 25) {
            healthWarningShown = false;
        }

        if (currentPet.getHappiness() < 25 && !happinessWarningShown) {
            showWarningPage("/resources/stats-happiness.fxml");
            happinessWarningShown = true;
        } else if (currentPet.getHappiness() >= 25) {
            happinessWarningShown = false;
        }

        if (currentPet.getFullness() < 25 && !hungerWarningShown) {
            showWarningPage("/resources/stats-hunger.fxml");
            hungerWarningShown = true;
        } else if (currentPet.getFullness() >= 25) {
            hungerWarningShown = false;
        }

        if (currentPet.getSleep() < 25 && !sleepWarningShown) {
            showWarningPage("/resources/stats-sleep.fxml");
            sleepWarningShown = true;
        } else if (currentPet.getSleep() >= 25) {
            sleepWarningShown = false;
        }
    }


    private void updateBarColor(ProgressBar bar, double value) {
        if (value < 25) {
            bar.setStyle("-fx-accent: red;");
        } else {
            bar.setStyle("-fx-accent: green;");
        }
    }

    private void showWarningPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage warningStage = new Stage();
            warningStage.setScene(new Scene(root));
            warningStage.setTitle("Warning");
            warningStage.show();
        } catch (IOException e) {
            System.err.println("Warning page not found: " + fxmlFile);
        }
    }

    private void checkPetStatus() {
        if (currentPet.isDead() && !gameOverDisplayed) {
            gameOverDisplayed = true; // Ensure the game-over logic runs only once
            if (timeline != null) { timeline.stop(); }
            // Update the pet image to the dead pet image
            updatePetImage();
            // Show the pet death warning dialog
            showPetDeathWarningDialog();
            // Disable all buttons after pet death
            disableButtons();
        }
    }


    private void disableCommandsForAngryState() {
        // Disable feed, exercise, and any other command that shouldn't be available when angry
        feedButton.setDisable(true);
        exerciseButton.setDisable(true);
        vetButton.setDisable(true);
        bedButton.setDisable(true); // Prevent sleep-related actions
    }

    private void enableCommandsForNormalState() {
        // Enable all buttons that should be active in the normal state
        feedButton.setDisable(false);
        giftButton.setDisable(false);
        playButton.setDisable(false);
        vetButton.setDisable(false);
        bedButton.setDisable(false);
        exerciseButton.setDisable(false);

    }

    private void disableAllCommandsExceptBack() {
        feedButton.setDisable(true);
        giftButton.setDisable(true);
        playButton.setDisable(true);
        vetButton.setDisable(true);
        exerciseButton.setDisable(true);
        bedButton.setDisable(true);
    }


    private void showPetDeathWarningDialog() {
        // Ensure this runs on the JavaFX Application thread, outside of animations/layout
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/pet-death-warning.fxml"));
            Parent dialogRoot = null;
            try {
                dialogRoot = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Create a new scene for the dialog
            Scene dialogScene = new Scene(dialogRoot);

            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Your Pet Has Passed Away");
            dialogStage.setScene(dialogScene);

            // Set modality to block input events to other stages
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(petName.getScene().getWindow());

            // Event handler for when the dialog is closed
            dialogStage.setOnHiding(event -> {
                // Navigate to the main menu without closing the current window
                navigateToMainMenu();
            });


            // Show the dialog (non-blocking)
            dialogStage.show();
        });
    }

    private void handleCooldown(Button button, boolean isActionActive, int cooldownTime, String buttonText, boolean isPetSleeping) {
        if (isPetSleeping) {
            setButtonDisabledState(button, buttonText + " (Sleeping)");
        } else if (isActionActive) {
            setButtonDisabledState(button, buttonText + " (Cooldown)");
            startCooldownTimer(button, cooldownTime); // Start cooldown
        } else {
            setButtonEnabledState(button, buttonText);
        }
    }

    private void setButtonDisabledState(Button button, String buttonText) {
        button.setDisable(true);
        button.setOpacity(0.5);
        button.setText(buttonText);
    }

    private void setButtonEnabledState(Button button, String buttonText) {
        button.setDisable(false);
        button.setOpacity(1.0);
        button.setText(buttonText);
    }

    private void navigateToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/main_menu.fxml"));
            Parent mainMenuRoot = loader.load();
            Stage currentStage = (Stage) petName.getScene().getWindow();
            if (timeline != null) { timeline.stop(); }
            currentStage.setScene(new Scene(mainMenuRoot));
            currentStage.setTitle("Main Menu");
        } catch (IOException e) {
            System.err.println("Error navigating to main menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInventoryButtonAction() throws IOException {
        // Load the inventory FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/inventory.fxml"));
        AnchorPane inventoryPane = loader.load();

        InventoryController inventoryController = loader.getController();
        this.inventoryController = inventoryController;

        // Set up a scene or popup for the inventory window
        Scene inventoryScene = new Scene(inventoryPane);
        Stage stage = new Stage();
        stage.setTitle("Inventory");
        stage.setScene(inventoryScene);
        stage.show();
    }



    private void disableButtons() {
        feedButton.setDisable(true);
        giftButton.setDisable(true);
        playButton.setDisable(true);
        vetButton.setDisable(true);
        bedButton.setDisable(true);
        exerciseButton.setDisable(true);
    }

    private void initializeButtonHandlers() {
        feedButton.setOnAction(event -> feedPet());
        giftButton.setOnAction(event -> giveGift());
        playButton.setOnAction(event -> playWithPet());
        vetButton.setOnAction(event -> takeToVet());
        bedButton.setOnAction(event -> putToBed());
        exerciseButton.setOnAction(event -> exercisePet());
    }

    private void feedPet() {
        if (currentPet.isDead() || currentPet.getCurrentState() == PetState.SLEEPING) { return; }

        // Check if inventoryController is available and has food items
        if (inventoryController != null && inventoryController.hasAvailableItem("food")) {
            if (currentPet.getFullness() < 100) {
                // Find the first available food item
                int foodIndex = inventoryController.getFirstAvailableFoodIndex();
                // Consume the food item
                inventoryController.feedItem(foodIndex);

                currentPet.increaseFullness(20);
                currentPet.increaseHappiness(5); // Feeding usually increases happiness
                score += 10;
                updateStats();
            }
        } else {
            // No food items available, disable the feed button
            feedButton.setDisable(true);
        }
        updateButtonState();
    }



    private void giveGift() {
        if (currentPet.isDead() || currentPet.getCurrentState() == PetState.SLEEPING) { return; }

        // Check if inventoryController is available and has gift items
        if (inventoryController != null && inventoryController.hasAvailableItem("gift")) {
            // Find the first available gift item
            int giftIndex = inventoryController.getFirstAvailableGiftIndex();
            // Consume the gift item
            inventoryController.giftItem(giftIndex);

            currentPet.increaseHappiness(15);
            score += 15;
            updateStats();
        } else {
            // No gift items available, disable the gift button
            giftButton.setDisable(true);
        }
        updateButtonState();
    }



    private void updateButtonText(Button button, String text) {
        button.setText(text);
    }

    private void playWithPet() {
        if (currentPet.getCurrentState() == PetState.ANGRY || currentPet.isDead()) { return; }
        if (isPlayCooldownActive) { return; }

        if (currentPet.getHappiness() < 100 && currentPet.getFullness() > 10) {
            currentPet.increaseHappiness(20);
            currentPet.decreaseFullness(10);
            currentPet.decreaseSleep(5);
            score += 20;
            updateStats();
            // Start cooldown for the "Play" button
            isPlayCooldownActive = true;
            updateButtonState(); // Immediately update button state after cooldown starts
            startCooldownTimer(playButton, PLAY_COOLDOWN_TIME); // Start cooldown
        }
    }

    private void takeToVet() {
        if (currentPet.getCurrentState() == PetState.ANGRY || currentPet.isDead()) { return; }
        if (isVetCooldownActive) { return; }

        if (currentPet.getHealth() < 100) {
            currentPet.increaseHappiness(10);
            currentPet.increaseFullness(5);
            currentPet.increaseHealth(30);
            score += 25;
            updateStats();
            // Start cooldown for the "Take to the Vet" button
            isVetCooldownActive = true;
            updateButtonState(); // Immediately update button state after cooldown starts
            startCooldownTimer(vetButton, VET_COOLDOWN_TIME); // Start cooldown
        }
    }

    private void startCooldownTimer(Button button, int cooldownTime) {
        PauseTransition pause = new PauseTransition(Duration.seconds(cooldownTime));
        pause.setOnFinished(event -> {
            // After cooldown period, reset the button state
            if (button == vetButton) { isVetCooldownActive = false; }
            else if (button == playButton) { isPlayCooldownActive = false; }
            // Update button state
            updateButtonState(); // Update the button states after cooldown
        });
        pause.play();

        // Update the button state immediately
        updateButtonState(); // Update the button states immediately
    }

    private void updateButtonState() {
        // Dead state: Disable all buttons
        if (currentPet.isDead()) {
            disableButtons();
            return;
        }

        if (currentPet.getCurrentState() == PetState.SLEEPING || currentPet.getSleep() <= 0) {
            disableAllCommandsExceptBack();
            return;
        }

        // Angry state: Disable specific commands
        if (currentPet.getCurrentState() == PetState.ANGRY) {
            disableCommandsForAngryState();
            return;
        }

        // Normal state: Enable commands and apply cooldowns
        enableCommandsForNormalState();

        if (inventoryController != null) {
            if (!inventoryController.hasAvailableItem("food")) { feedButton.setDisable(true); }
            if (!inventoryController.hasAvailableItem("gift")) { giftButton.setDisable(true); }
        }

        if (isPlayCooldownActive) { setButtonDisabledState(playButton, "Play (Cooldown)"); }
        if (isVetCooldownActive) { setButtonDisabledState(vetButton, "Vet (Cooldown)"); }
    }

    private void putToBed() {
        // If the pet is already fully asleep, don't do anything
        if (currentPet.getSleep() >= 100) { return; }
        disableAllCommandsExceptBack();
        currentPet.setCurrentState(PetState.SLEEPING);
        score += 10;
        updateStats();
        updateButtonState();
        checkSleepAndHealth();
    }

    private void checkSleepAndHealth() {
        // Start a Timeline or periodically check sleep status and health status
        Timeline healthMonitorTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // If the pet's health drops to 0, end the game
            if (currentPet.isDead()) {
                endGame();
            }

            // If the pet's sleep reaches 100 and health is restored, wake the pet up
            if (currentPet.getSleep() >= 100 && currentPet.getHealth() >= 100) {
                wakeUpPet();
            } else {
                // Gradually restore sleep (if sleep < 100), and handle health restoration.
                if (currentPet.getSleep() < 100) {
                    currentPet.increaseSleep(5); // Restore 10 points of sleep per tick
                }

                if (currentPet.getHealth() < 100) {
                    currentPet.increaseHealth(3); // Restore health while the pet sleeps
                }
            }
        }));

        healthMonitorTimeline.setCycleCount(Timeline.INDEFINITE);
        healthMonitorTimeline.play();
    }


    private void wakeUpPet() {
        currentPet.setCurrentState(PetState.ALIVE);
        enableCommandsForNormalState();
        updateStats();
        updateButtonState();
        updatePetImage();
    }



    private void endGame() {
        // Stop the Timeline to halt all updates
        if (timeline != null) {
            timeline.stop();
        }

        // Show the pet death warning dialog
        showPetDeathWarningDialog();

        // Disable all buttons after pet death
        disableButtons();
    }

    private void exercisePet() {
        if (currentPet.getSleep() > 10 && currentPet.getFullness() > 10) {
            currentPet.increaseHappiness(10);
            currentPet.decreaseFullness(10);
            currentPet.decreaseSleep(15);
            currentPet.increaseHealth(5);
            score += 15;
            updateStats();
        }
    }

    @FXML
    private void handleBackButtonAction() {
        // Stop the Timeline to halt all updates
        if (timeline != null) {
            timeline.stop();
        }

        try {
            saveCurrentGame();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Load the main menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/main_menu.fxml"));
            Parent mainMenuRoot = loader.load();

            // Get the current stage from the back button and replace the scene
            Stage currentStage = (Stage) petName.getScene().getWindow();
            currentStage.setScene(new Scene(mainMenuRoot));
            currentStage.setTitle("Main Menu");
        } catch (IOException e) {
            System.err.println("Error loading the main menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveCurrentGame() throws IOException {
        // This will just check src directory, but it's already there.
        save.SaveManager.ensureSaveDirectoryExists();

        String currentPetType = currentPet.getType();
        String currentPetName = currentPet.getName();
        int currentScore = this.score;

        int maxSlots = 3;
        boolean saved = false;

        for (int slot = 0; slot < maxSlots; slot++) {
            if (!save.SaveManager.isSlotOccupied(slot)) {
                writeGameToSlot(slot, currentPetType, currentPetName, currentScore);
                saved = true;
                break;
            } else {
                SavedGameData slotData = loadGameFromSlot(slot);
                if (slotData != null) {
                    if (slotData.getPetType().equals(currentPetType) && slotData.getPetName().equals(currentPetName)) {
                        writeGameToSlot(slot, currentPetType, currentPetName, currentScore);
                        saved = true;
                        break;
                    }
                }
            }
        }

        if (!saved && maxSlots > 0) {
            writeGameToSlot(0, currentPetType, currentPetName, currentScore);
        }

        System.out.println("Game saved successfully.");
    }


    private void writeGameToSlot(int slot, String petType, String petName, int score) throws IOException {
        Path saveFile = save.SaveManager.getSaveFilePath(slot);
        String saveData = "PetType: " + petType + "\n" +
                "PetName: " + petName + "\n" +
                "Score: " + score;
        Files.writeString(saveFile, saveData);
    }

    private SavedGameData loadGameFromSlot(int slot) {
        try {
            Path saveFile = save.SaveManager.getSaveFilePath(slot);
            if (Files.exists(saveFile)) {
                List<String> lines = Files.readAllLines(saveFile);
                String petType = "";
                String petName = "";
                int score = 0;

                for (String line : lines) {
                    if (line.startsWith("PetType:")) {
                        petType = line.substring("PetType:".length()).trim();
                    } else if (line.startsWith("PetName:")) {
                        petName = line.substring("PetName:".length()).trim();
                    } else if (line.startsWith("Score:")) {
                        score = Integer.parseInt(line.substring("Score:".length()).trim());
                    }
                }

                return new SavedGameData(petType, petName, score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Helper class to store loaded save data
    private static class SavedGameData {
        private final String petType;
        private final String petName;
        private final int score;

        public SavedGameData(String petType, String petName, int score) {
            this.petType = petType;
            this.petName = petName;
            this.score = score;
        }

        public String getPetType() { return petType; }
        public String getPetName() { return petName; }
        public int getScore() { return score; }
    }



}