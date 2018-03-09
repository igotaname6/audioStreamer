package com.codecool.audioStreamClientUtils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class ClientView {

    private Stage stage;
    private Button playButton;
    private Slider volSlider;

    public Stage getStage() {
        return stage;
    }

    public void setScene(Stage stage) throws IOException {

        Pane root = FXMLLoader.load(getClass().getResource("/playerWindow.fxml"));

        stage.setScene(new Scene(root));

        stage.setTitle("AudioStream Player");

        System.out.println(root.getChildren());

        playButton = (Button) root.getChildren().get(0);
        setPlayButton();
        volSlider = (Slider) root.getChildren().get(1);
        setVolSlider();
        stage.show();
        this.stage = stage;
    }

    private void setPlayButton() {
        playButton.setText("||");
    }

    private void setVolSlider() {
        volSlider.setMin(0);
        volSlider.setMax(100);
        volSlider.setBlockIncrement(10);
        volSlider.setSnapToTicks(false);
        volSlider.setMajorTickUnit(50);
        volSlider.setMinorTickCount(0);
        volSlider.setShowTickLabels(true);
        volSlider.setShowTickMarks(true);
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Slider getVolSlider() {
        return volSlider;
    }
}
