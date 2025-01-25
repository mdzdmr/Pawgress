package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PetSaveData {
    public static void saveGame(String petType, String petName, int score) {
        String saveData = "PetType: " + petType + "\n" +
                "PetName: " + petName + "\n" +
                "Score: " + score;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("saveGame.txt"))) {
            writer.write(saveData);
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving the game: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        saveGame("Dog", "Fluffy", 200);
    }
}
