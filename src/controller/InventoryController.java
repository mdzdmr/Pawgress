package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class InventoryController {

    @FXML
    private Text food1;
    @FXML
    private Text food2;
    @FXML
    private Text food3;
    @FXML
    private Text gift1;
    @FXML
    private Text gift2;
    @FXML
    private Text gift3;

    public int food1Count = 0, food2Count = 0, food3Count = 0;
    public int gift1Count = 0, gift2Count = 0, gift3Count = 0;

    public void initialize() {
        startPeriodicUpdates();
    }
    public int getFirstAvailableFoodIndex() {
        if (food1Count > 0) return 1;
        if (food2Count > 0) return 2;
        if (food3Count > 0) return 3;
        return -1; // No food available
    }

    public int getFirstAvailableGiftIndex() {
        if (gift1Count > 0) return 1;
        if (gift2Count > 0) return 2;
        if (gift3Count > 0) return 3;
        return -1; // No gift available
    }

    private void startPeriodicUpdates() {
        // Increment Food 1 and Gift 1 every 15 seconds
        Timeline timeline30s = new Timeline(new KeyFrame(Duration.seconds(30), e -> {
            incrementFood(1);
            incrementGift(1);
        }));
        timeline30s.setCycleCount(Timeline.INDEFINITE);
        timeline30s.play();

        // Increment Food 2 and Gift 2 every 30 seconds
        Timeline timelines75s = new Timeline(new KeyFrame(Duration.seconds(75), e -> {
            incrementFood(2);
            incrementGift(2);
        }));
        timelines75s.setCycleCount(Timeline.INDEFINITE);
        timelines75s.play();

        // Increment Food 3 and Gift 3 every 60 seconds
        Timeline timeline120s = new Timeline(new KeyFrame(Duration.seconds(120), e -> {
            incrementFood(3);
            incrementGift(3);
        }));
        timeline120s.setCycleCount(Timeline.INDEFINITE);
        timeline120s.play();
    }

    private void incrementFood(int index) {
        switch (index) {
            case 1 -> {
                food1Count++;
                food1.setText(String.valueOf(food1Count));
            }
            case 2 -> {
                food2Count++;
                food2.setText(String.valueOf(food2Count));
            }
            case 3 -> {
                food3Count++;
                food3.setText(String.valueOf(food3Count));
            }
        }
    }

    private void incrementGift(int index) {
        switch (index) {
            case 1 -> {
                gift1Count++;
                gift1.setText(String.valueOf(gift1Count));
            }
            case 2 -> {
                gift2Count++;
                gift2.setText(String.valueOf(gift2Count));
            }
            case 3 -> {
                gift3Count++;
                gift3.setText(String.valueOf(gift3Count));
            }
        }
    }

    // Method to check if there are items available before feeding or gifting
    public boolean hasAvailableItem(String type) {
        return switch (type) {
            case "food" -> food1Count > 0 || food2Count > 0 || food3Count > 0;
            case "gift" -> gift1Count > 0 || gift2Count > 0 || gift3Count > 0;
            default -> false;
        };
    }

    // Method to feed an item (decrement the count of the specified food item)
    public void feedItem(int index) {
        if (hasAvailableItem("food")) {
            switch (index) {
                case 1 -> {
                    if (food1Count > 0) {
                        food1Count--;
                        food1.setText(String.valueOf(food1Count));
                    }
                }
                case 2 -> {
                    if (food2Count > 0) {
                        food2Count--;
                        food2.setText(String.valueOf(food2Count));
                    }
                }
                case 3 -> {
                    if (food3Count > 0) {
                        food3Count--;
                        food3.setText(String.valueOf(food3Count));
                    }
                }
            }
        }
    }

    // Method to gift an item (decrement the count of the specified gift item)
    public void giftItem(int index) {
        if (hasAvailableItem("gift")) {
            switch (index) {
                case 1 -> {
                    if (gift1Count > 0) {
                        gift1Count--;
                        gift1.setText(String.valueOf(gift1Count));
                    }
                }
                case 2 -> {
                    if (gift2Count > 0) {
                        gift2Count--;
                        gift2.setText(String.valueOf(gift2Count));
                    }
                }
                case 3 -> {
                    if (gift3Count > 0) {
                        gift3Count--;
                        gift3.setText(String.valueOf(gift3Count));
                    }
                }
            }
        }
    }
}
