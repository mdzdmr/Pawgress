package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TutorialController {

    @FXML private StackPane slideStack;
    @FXML private VBox slide1, slide2, slide3, slide4;
    private int currentSlideIndex = 0;
    private List<VBox> slides = new ArrayList<>();

    @FXML
    public void initialize() {
        slides.add(slide1);
        slides.add(slide2);
        slides.add(slide3);
        slides.add(slide4);
        showSlide(currentSlideIndex);
    }

    private void showSlide(int index) {
        for (VBox slide : slides) {
            slide.setVisible(false);
        }
        if (index >= 0 && index < slides.size()) {
            slides.get(index).setVisible(true);
        }
    }

    @FXML
    private void goToNextSlide() {
        if (currentSlideIndex < slides.size() - 1) {
            currentSlideIndex++;
            showSlide(currentSlideIndex);
        }
    }

    @FXML
    private void goToPreviousSlide() {
        if (currentSlideIndex > 0) {
            currentSlideIndex--;
            showSlide(currentSlideIndex);
        }
    }

    @FXML
    private void closeTutorial(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/main_menu.fxml"));
        Parent mainMenuRoot = loader.load();

        Scene mainMenuScene = new Scene(mainMenuRoot);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(mainMenuScene);
    }
}
