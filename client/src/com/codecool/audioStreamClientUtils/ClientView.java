package com.codecool.audioStreamClientUtils;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;


@Service
public class ClientView {

    private Stage stage;
    private Button playButton;
    private Slider volSlider;

    public Stage getStage() {
        return stage;
    }

    public void setScene(Stage stage) {
        setPlayButton();
        setVolSlider();
        stage.setTitle("AudioStream Player");
        this.stage = stage;
        StackPane root = new StackPane();
        root.getChildren().add(playButton);
        root.getChildren().add(volSlider);
//        stage.setScene(new Scene());
    }

    private void setPlayButton() {
        playButton = new Button(">");

    }

    private void setVolSlider() {
        volSlider = new Slider();
        volSlider.setMin(0);
        volSlider.setMax(100);
        volSlider.setBlockIncrement(10);
        volSlider.setSnapToTicks(false);
        volSlider.setMajorTickUnit(50);
        volSlider.setMinorTickCount(0);
        volSlider.setShowTickLabels(true);
        volSlider.setShowTickMarks(true);
    }

    public void show() {
        stage.show();
    }
}
