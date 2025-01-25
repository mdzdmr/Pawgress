package save;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONObject;

import model.Pet;

public class GameState {

    private JSONObject state;
    private String filePath;

    // sate variables
    private Pet statePet;
    private double playTime;

    /**
     * <p>
     * Constructor to be used when loading a previous save </p>
     *
     * @param filePath - string representing where the save file is stored
     *
     */
    public GameState(String filePath) {
        this.filePath = filePath;
        try {
            Path path = Path.of(filePath);
            String s = Files.readString(path);
            state = new JSONObject(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        playTime = state.getDouble("PlayTime");
        statePet = jsonToPet(state);
    }

    /**
     * <p>
     * Constructor to be used when creating a new save </p>
     *
     * @param filePath - string representing where the save file is stored
     * @param pet - pet chosen by the player
     */
    public GameState(String filePath, Pet pet) {
        this.filePath = filePath;
        try {
            Path path = Path.of(filePath);
            String s = Files.readString(path);
            state = new JSONObject(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        state.put("PlayTime", 0d);
        state.put("Pet", petToJSON(pet));
    }

    public JSONObject getState() { //TODO remove
        return state;
    }

    /**
     * @return total play time of game state
     *
     */
    public double getPlayTime() {
        return  playTime;
    }

    /**
     * @param time - total play time
     *
     */
    public void setPlayTime(double time) {
        state.put("PlayTime", time);
        playTime = time;
    }

    /**
     * @return pet object from game state
     *
     */
    public Pet getPet() {
        return statePet;
    }

    /**
     * <p>
     * Writes the current game state to the json save file</p>
     *
     */
    public void saveState() {
        try {
            FileWriter file = new FileWriter(filePath);
            file.write(state.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return pet object created from json object
     *
     */
    private Pet jsonToPet(JSONObject json) {
        JSONObject petJson = (JSONObject) json.get("Pet");
        Pet pet = new Pet(petJson.getString("Type"), petJson.getString("Description"), petJson.getString("ImagePath"), petJson.getDouble("HungerDecayRate"), petJson.getDouble("SleepDecayRate"), petJson.getDouble("HappinessDecayRate"));
        return pet;
    }

    /**
     * @return json object created from pet object
     *
     */
    private JSONObject petToJSON(Pet pet) {
        JSONObject petJson = new JSONObject();
        petJson.put("Type", pet.getType());
        petJson.put("Name", pet.getName());
        petJson.put("Description", pet.getDescription());
        petJson.put("Health", pet.getHealth());
        petJson.put("Sleep", pet.getSleep());
        petJson.put("Fullness", pet.getFullness());
        petJson.put("Happiness", pet.getHappiness());
        petJson.put("ImagePath", pet.getImagePath());
        petJson.put("HungerDecayRate", pet.getHungerDecayRate());
        petJson.put("SleepDecayRate", pet.getSleepDecayRate());
        petJson.put("HappinessDecayRate", pet.getHappinessDecayRate());
        return petJson;
    }

}

