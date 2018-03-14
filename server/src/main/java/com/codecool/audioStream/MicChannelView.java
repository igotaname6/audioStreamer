package com.codecool.audioStream;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MicChannelView {

    Button onAir;
    Button fldbck;
    Button addChannel;
    Slider gain;
    Stage stage;

    public void setStage(Stage stage) throws IOException {

        Pane root = FXMLLoader.load(getClass().getResource("/micWindow.fxml"));

        stage.setScene(new Scene(root));
        stage.setTitle("Mic Channel");
        this.stage = stage;

        onAir = (Button) root.getChildren()
                .stream()
                .filter(node -> node instanceof Button && ((Button) node).getText().equalsIgnoreCase("ON AIR"))
                .toArray()[0];

        fldbck = (Button) root.getChildren()
                .stream()
                .filter(node -> node instanceof Button && ((Button) node).getText().equalsIgnoreCase("FLDBCK"))
                .toArray()[0];

        addChannel = (Button) root.getChildren()
                .stream()
                .filter(node -> node instanceof Button && ((Button) node).getText().equalsIgnoreCase("Add File Channel"))
                .toArray()[0];

        gain = (Slider) root.getChildren()
                .stream()
                .filter(node -> node instanceof Slider)
                .toArray()[0];
    }

    public Button getOnAir() {
        return onAir;
    }

    public void setOnAir(Button onAir) {
        this.onAir = onAir;
    }

    public Button getFldbck() {
        return fldbck;
    }

    public void setFldbck(Button fldbck) {
        this.fldbck = fldbck;
    }

    public Button getAddChannel() {
        return addChannel;
    }

    public void setAddChannel(Button addChannel) {
        this.addChannel = addChannel;
    }

    public Slider getGain() {
        return gain;
    }

    public void setGain(Slider gain) {
        this.gain = gain;
    }

    public void showWindow() {
        stage.show();
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

    public Stage getStage() {
        return stage;
    }
}
