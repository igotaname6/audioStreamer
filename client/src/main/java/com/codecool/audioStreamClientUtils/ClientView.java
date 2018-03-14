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

    public void setScene(Stage stage) throws IOException {

        Pane root = FXMLLoader.load(getClass().getResource("/playerWindow.fxml"));

        stage.setScene(new Scene(root));

        stage.setTitle("AudioStream Player");

        playButton = (Button) root.getChildren().get(0);
        setPlayButton();
        volSlider = (Slider) root.getChildren().get(1);
        this.stage = stage;
        stage.show();
    }

    private void setPlayButton() {
        playButton.setText("||");
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Slider getVolSlider() {
        return volSlider;
    }

    public Stage getStage() {
        return stage;
    }
}
