package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import model.Pet;
import model.PetFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PetSelectionController {

    @FXML private ImageView petImageView1, petImageView2, petImageView3;
    @FXML private Label petNameLabel1, petDescriptionLabel1;
    @FXML private Label petNameLabel2, petDescriptionLabel2;
    @FXML private Label petNameLabel3, petDescriptionLabel3;
    @FXML private TextField petNameField;
    @FXML private Button selectButton, backButton;

    private List<Pet> availablePets;
    private Pet selectedPet;

    @FXML
    public void initialize() {
        availablePets = PetFactory.createAvailablePets();

        if (availablePets == null || availablePets.size() < 3) {
            System.err.println("Error: Not enough pets available to load.");
            return;
        }

        // Load pet data into the UI
        loadPetData(0, petImageView1, petNameLabel1, petDescriptionLabel1);
        loadPetData(1, petImageView2, petNameLabel2, petDescriptionLabel2);
        loadPetData(2, petImageView3, petNameLabel3, petDescriptionLabel3);
    }

    private void loadPetData(int index, ImageView imageView, Label nameLabel, Label descriptionLabel) {
        Pet pet = availablePets.get(index);

        try (InputStream imageStream = getClass().getResourceAsStream("/resources/" + pet.getImagePath())) {
            if (imageStream != null) {
                imageView.setImage(new Image(imageStream));
            } else {
                System.err.println("Error: Could not find image at path " + pet.getImagePath());
            }
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }

        nameLabel.setText(pet.getType());
        descriptionLabel.setText(pet.getDescription());

        // Set up the click event to select the pet
        imageView.setOnMouseClicked(event -> {
            selectedPet = pet;
            highlightSelectedPet(imageView);
        });
    }

    private void highlightSelectedPet(ImageView selectedImageView) {
        // Reset opacity for all pet images to indicate unselected state
        petImageView1.setOpacity(0.5);
        petImageView2.setOpacity(0.5);
        petImageView3.setOpacity(0.5);

        // Set full opacity for the selected pet image
        selectedImageView.setOpacity(1.0);
    }

    @FXML
    private void handleSelectPet(ActionEvent event) {
        try {
            if (selectedPet == null) {
                System.out.println("Please select a pet first.");
                return;
            }

            String petName = petNameField.getText();
            if (petName == null || petName.trim().isEmpty()) {
                System.out.println("Please enter a name for your pet.");
                return;
            }

            // Set the pet's name
            selectedPet.setName(petName);

            // Navigate to the gameplay screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gameplay.fxml"));
            Parent gameplayRoot = loader.load();

            // Pass the selected pet to the GameplayController
            GameplayController gameplayController = loader.getController();
            gameplayController.initializePet(selectedPet);

            Scene gameplayScene = new Scene(gameplayRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(gameplayScene);
        } catch (IOException e) {
            System.err.println("Error navigating to gameplay screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/main_menu.fxml"));
            Parent mainMenuRoot = loader.load();

            Scene mainMenuScene = new Scene(mainMenuRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(mainMenuScene);
        } catch (IOException e) {
            System.err.println("Error navigating to main menu: " + e.getMessage());
        }
    }
}
