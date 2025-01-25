# Pawgress

## Description
Pawgress is a task and reward-based game where players care for their virtual pets. The goal is to encourage users to develop a sense of responsibility by managing various tasks associated with pet care, such as feeding, grooming, and ensuring the pet’s happiness and health. Players must interact regularly with their pets to maintain their well-being; neglecting the pet could lead to adverse consequences, making the experience both fun and educational!

## Required Libraries and Tools
- **Java Development Kit (JDK)** - Version 23 
- **JavaFX** - Version 23.0.1 for the GUI elements
- **JUnit** - Version 5.11.3 for testing

Make sure all the above libraries are installed and included in your project’s classpath.

## Building the Software
### Prerequisites
1. Ensure you have Java JDK 23 and JavaFX installed.
2. Clone the repository from your university's GitLab:

   ```bash
   git clone https://gitlab.sci.uwo.ca/courses/2024/09/COMPSCI2212/group23.git
   cd pawgress
    ```

## Build & Run Instructions
1. Open the project in your preferred IDE (IntelliJ, Eclipse, or VSCode).
2. Ensure JavaFX libraries are configured correctly in the project settings.
3. Ensure the org.json library is in the build path.
4. Run the program from the 'Main' file.

## User Guide
### Starting the Game
1. Launch the game as described in the "Running the Compiled Software" section.
2. From the main menu, choose:
    - New Game: Start a fresh game.
    - Load Game: Load a previously saved game.
    - Tutorial: Understand how to play the game.
    - Parental Controls: Use to set restrictions.
    - Exit: Leave the game.
    
### In-Game Actions
1. Feed: Increases your pet's hunger level and decreases its happiness slightly.
2. Play: Boosts happiness but reduces hunger and sleep stats.
3. Take to Vet: Restores health at the cost of a cooldown period.
4. Go to Bed: Replenishes sleep and health over time.
5. Exercise: Improves happiness and health but reduces hunger and sleep.

### Warnings and Game Over
1. A warning screen will alert you if any stat is critically low.
2. If your pet's health reaches zero, the game ends, and a pet death screen is shown.

## Parental Controls
We've hardcoded a password for our game 'Pawgress'. Any parent can access this page by using the password 'parent1234'. The parent then has the choice to either:
1. Set time limits.
2. Check screen time.
3. Revive any pet.
<br>
Thank you for playing Pawgress! For further assistance or questions, please contact the project team.

