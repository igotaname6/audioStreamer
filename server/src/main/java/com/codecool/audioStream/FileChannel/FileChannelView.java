package com.codecool.audioStream.FileChannel;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@Service
public class FileChannelView {

    Stage stage;
    Button onAir;
    Button fldbck;
    Button addFile;
    Button play;
    Slider bar;
    Slider gain;
    Text text;
    FileChooser fileChooser;
    Text playTime;
    Text elapsedTime;

    public void setStage() throws IOException {

        Pane root = FXMLLoader.load(getClass().getResource("/fileChannelWindow.fxml"));

        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("File Channel");

        onAir = (Button) root.getChildren()
                .stream()
                .filter(node -> node instanceof Button && ((Button) node).getText().equalsIgnoreCase("ON AIR"))
                .toArray()[0];

        fldbck = (Button) root.getChildren()
                .stream()
                .filter(node -> node instanceof Button && ((Button) node).getText().equalsIgnoreCase("FLDBCK"))
                .toArray()[0];

        addFile = (Button) root.getChildren()
                .stream()
                .filter(node -> node instanceof Button && ((Button) node).getText().equalsIgnoreCase("+"))
                .toArray()[0];

        play = (Button) root.getChildren()
                .stream()
                .filter(node -> node instanceof Button && ((Button) node).getText().equalsIgnoreCase(">"))
                .toArray()[0];
        gain = (Slider) root.getChildren()
                .stream()
                .filter(node -> node instanceof Slider)
                .toArray()[0];
        bar = (Slider) root.getChildren()
                .stream()
                .filter(node -> node instanceof Slider)
                .toArray()[1];
        text = (Text) root.getChildren()
                .stream()
                .filter(node -> node instanceof Text)
                .toArray()[0];
        playTime = (Text) root.getChildren()
                .stream()
                .filter(node -> node instanceof Text)
                .toArray()[1];
        elapsedTime = (Text) root.getChildren()
                .stream()
                .filter(node -> node instanceof Text)
                .toArray()[2];
        text.setText("No file added");
    }

    public void setFilename(String filename) {
        this.text.setText(filename);
        this.stage.setTitle(filename);
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime.setText(elapsedTime);
    }

    public void setPlayTime(String playTime) {
        this.playTime.setText(playTime);
    }

    public Button getPlay() {
        return play;
    }

    public Button getOnAir() {
        return onAir;
    }

    public Button getFldbck() {
        return fldbck;
    }

    public Button getAddFile() {
        return addFile;
    }

    public Slider getBar() {
        return bar;
    }

    public Slider getGain() {
        return gain;
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    public void setReleasedOnAirButton() {
        onAir.setStyle("-fx-background-color: gainsboro");
    }

    public void setPressedOnAirButton() {
        onAir.setStyle("-fx-background-color: red");
    }

    public void setReleasedFldbckButton() {
        fldbck.setStyle("-fx-background-color: gainsboro");
    }

    public void setPressedFldbckButton() {
        fldbck.setStyle("-fx-background-color: forestgreen");
    }

    public void setFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio files", "*.wav"));
        fileChooser.setTitle("Choose file");
    }

    public Stage getStage() {
        return stage;
    }

    public void showWindow() {
        this.stage.show();
    }
}
