package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class RevivePetController {

    // FXML components for each pet
    @FXML private ImageView petImageView1;
    @FXML private Label petNameLabel1;
    @FXML private Label petScoreLabel1;

    @FXML private ImageView petImageView2;
    @FXML private Label petNameLabel2;
    @FXML private Label petScoreLabel2;

    @FXML private ImageView petImageView3;
    @FXML private Label petNameLabel3;
    @FXML private Label petScoreLabel3;

    private String selectedPetName;
    private int selectedPetScore;

    // Handle pet 1 click
    @FXML
    private void handlePet1Click() {
        // Set the selected pet's name and score when pet 1 is clicked
        selectedPetName = petNameLabel1.getText();
        selectedPetScore = Integer.parseInt(petScoreLabel1.getText().split(": ")[1]);
        System.out.println("Selected " + selectedPetName + " with score " + selectedPetScore);
    }

    // Handle pet 2 click
    @FXML
    private void handlePet2Click() {
        // Set the selected pet's name and score when pet 2 is clicked
        selectedPetName = petNameLabel2.getText();
        selectedPetScore = Integer.parseInt(petScoreLabel2.getText().split(": ")[1]);
        System.out.println("Selected " + selectedPetName + " with score " + selectedPetScore);
    }

    // Handle pet 3 click
    @FXML
    private void handlePet3Click() {
        // Set the selected pet's name and score when pet 3 is clicked
        selectedPetName = petNameLabel3.getText();
        selectedPetScore = Integer.parseInt(petScoreLabel3.getText().split(": ")[1]);
        System.out.println("Selected " + selectedPetName + " with score " + selectedPetScore);
    }

    // Revive selected pet (remove the pet's image)
    @FXML
    private void revivePet(ActionEvent event) {
        if (selectedPetName != null) {
            // Logic to remove the pet's image based on the selected pet
            if (selectedPetName.equals(petNameLabel1.getText())) {
                petImageView1.setImage(null); // Remove image of pet 1
            } else if (selectedPetName.equals(petNameLabel2.getText())) {
                petImageView2.setImage(null); // Remove image of pet 2
            } else if (selectedPetName.equals(petNameLabel3.getText())) {
                petImageView3.setImage(null); // Remove image of pet 3
            }

            // Optionally print out the name and score of the revived pet
            System.out.println("Revived " + selectedPetName + " with score " + selectedPetScore);
        } else {
            System.out.println("No pet selected to revive.");
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
