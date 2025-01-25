package model;

public class Pet {
    private String type;
    private String name;
    private String description;
    private double health;
    private double sleep;
    private double fullness;
    private double happiness;
    private double hungerDecayRate;
    private double sleepDecayRate;
    private double happinessDecayRate;
    private String imagePath;
    private PetState currentState;
    private int score;

    // Constructor
    public Pet(String type, String description, String imagePath, double hungerDecayRate, double sleepDecayRate, double happinessDecayRate) {
        this.type = type;
        this.description = description;
        this.imagePath = imagePath;
        this.health = 100;
        this.sleep = 100;
        this.fullness = 100;
        this.happiness = 100;
        this.hungerDecayRate = hungerDecayRate;
        this.sleepDecayRate = sleepDecayRate;
        this.happinessDecayRate = happinessDecayRate;
        this.currentState = PetState.ALIVE;
    }

    public Pet(String type, String name, int score) {
        this.type = type;
        this.name = name;
        this.description = ""; // Default description
        this.imagePath = "/resources/" + type.toLowerCase() + ".png"; // Default image path
        this.health = 100;
        this.sleep = 100;
        this.fullness = 100;
        this.happiness = 100;
        this.hungerDecayRate = 1.0; // Default decay rates
        this.sleepDecayRate = 1.0;
        this.happinessDecayRate = 1.0;
        this.currentState = PetState.ALIVE;
        this.score = score; // Set the score
    }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getType() { return type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public double getHealth() { return health; }
    public double getSleep() { return sleep; }
    public double getFullness() { return fullness; }
    public double getHappiness() { return happiness; }
    public String getImagePath() { return imagePath; }
    public double getHungerDecayRate() { return hungerDecayRate; }
    public double getSleepDecayRate() { return sleepDecayRate; }
    public double getHappinessDecayRate() { return happinessDecayRate; }

    public String getStateImagePath() {
        return switch (currentState) {
            case SLEEPING -> "/resources/" + type.toLowerCase() + "_sleeping.png";
            case HUNGRY -> "/resources/" + type.toLowerCase() + "_hungry.png";
            case ANGRY -> "/resources/" + type.toLowerCase() + "_angry.png";
            case DEAD -> "/resources/" + type.toLowerCase() + "_dead.png";
            default -> "/resources/" + type.toLowerCase() + ".png";
        };
    }

    public PetState getCurrentState() { return currentState; }

    public void setCurrentState(PetState currentState) { this.currentState = currentState; }

    // Stat Modification Methods
    public void increaseHealth(double amount) { this.health = clamp(this.health + amount); }
    public void decreaseHealth(double amount) { this.health = clamp(this.health - amount); }

    public void increaseSleep(double amount) { this.sleep = clamp(this.sleep + amount); }
    public void decreaseSleep(double amount) { this.sleep = clamp(this.sleep - amount); }

    public void increaseFullness(double amount) { this.fullness = clamp(this.fullness + amount); }
    public void decreaseFullness(double amount) { this.fullness = clamp(this.fullness - amount); }

    public void increaseHappiness(double amount) { this.happiness = clamp(this.happiness + amount); }
    public void decreaseHappiness(double amount) { this.happiness = clamp(this.happiness - amount); }

    // Clamp values between 0 and 100
    private double clamp(double value) { return Math.max(0, Math.min(100, value)); }

    public void tick() {
        if (currentState == PetState.SLEEPING) {
            handleSleepingState();
            if (sleep >= 100) { determineNextState(); }
            return;
        }
        decreaseFullness(hungerDecayRate);
        decreaseSleep(sleepDecayRate);
        decreaseHappiness(happinessDecayRate);
        // Health decay logic
        if (fullness <= 0) { decreaseHealth(2); }
        determineNextState();
    }


    private void determineNextState() {
        if (health <= 0) {
            currentState = PetState.DEAD;
        } else if (sleep >= 100) {
            currentState = PetState.ALIVE; // Wake up when fully rested
        } else if (sleep <= 0) {
            currentState = PetState.SLEEPING; // Automatically enter sleep state
        } else if (fullness <= 0) {
            currentState = PetState.HUNGRY;
        } else if (happiness <= 0) {
            currentState = PetState.ANGRY;
        } else {
            currentState = PetState.ALIVE; // Normal state
        }
    }

    // Handle sleep-specific logic
    public void handleSleepingState() {
        if (currentState == PetState.SLEEPING) {
            increaseSleep(10); // Restore sleep incrementally
            increaseHealth(1); // Optional: Health boost during sleep
        }
    }

    // Check if the pet is dead
    public boolean isDead() { return health <= 0; }

}
