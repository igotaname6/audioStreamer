package com.codecool.audioStream.FileChannel;

import org.springframework.stereotype.Controller;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

@Controller
public class FileChannelController {

    FileChannelView view;
    FileChannel player;

    public FileChannelController(FileChannelView view, FileChannel player) {
        this.view = view;
        this.player = player;
    }

    public void start() throws IOException {
        view.setStage();
        view.getStage().setOnCloseRequest(event -> player.close());

        player.setMasterGain(0);

        offAir();
        offFldbck();

        view.getOnAir().setOnAction(event -> {if (player.isOnAir()) offAir(); else onAir();});
        view.getFldbck().setOnAction(event -> {if (player.isFldbck()) offFldbck(); else onFldbck();});

        view.setFileChooser();
        view.getAddFile().setOnAction(event -> setFileStream(view.getFileChooser().showOpenDialog(view.getStage())));

        view.getPlay().setOnAction(event -> {if (player.isPlaying()) pause(); else play();});

        view.getGain().setMin(0);
        view.getGain().setMax(1);
        view.getGain().setValue(player.getMasterGain());
        view.getGain().valueProperty().addListener((observable, oldValue, newValue) -> player.setMasterGain(newValue.floatValue()));
        view.showWindow();
        new Thread(player).start();
    }

    public void pause() {
        player.setPlaying(false);
        view.getPlay().setText(">");
    }

    public void play() {
        player.setPlaying(true);
        view.getPlay().setText("||");
    }

    public void onAir() {
        player.setOnAir(true);
        view.setPressedOnAirButton();
    }

    public void offAir() {
        player.setOnAir(false);
        view.setReleasedOnAirButton();
    }

    public void onFldbck() {
        player.setFldbck(true);
        view.setPressedFldbckButton();
    }

    public void offFldbck() {
        player.setFldbck(false);
        view.setReleasedFldbckButton();
    }

    public void setFileStream(File file) {
        try {
            view.setFilename(file.getName());

            player.setFileStream(file);
            System.out.println(player.getFileStream());
            view.getBar().setMax(file.length());
            view.getBar().setMin(0);
            view.getBar().setValue(0);
            player.setProgresBar(view.getBar());
            view.getBar().valueProperty().addListener((observable, oldValue, newValue) -> player.setPosition(newValue.longValue()));
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }
}
