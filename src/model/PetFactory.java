package model;

import java.util.ArrayList;
import java.util.List;

public class PetFactory {
    public static List<Pet> createAvailablePets() {
        List<Pet> pets = new ArrayList<>();

        // Create Cow
        pets.add(new Pet(
                "Cow",
                "Gentle and nurturing.",
                "../resources/cow.png",
                0.5, // Hunger decay rate
                0.7, // Sleep decay rate
                0.6  // Happiness decay rate
        ));

        // Create Chick
        pets.add(new Pet(
                "Chick",
                "Peppy and curious.",
                "../resources/chick.png",
                0.6, // Hunger decay rate
                0.5,  // Sleep decay rate
                0.7  // Happiness decay rate
        ));

        // Create Squid(ward)
        pets.add(new Pet(
                "Squid(ward)",
                "Sassy and artistic.",
                "../resources/squid(ward).png",
                0.7, // Hunger decay rate
                0.6,  // Sleep decay rate
                0.8 // Happiness decay rate
        ));

        return pets;
    }
}
