package com.codecool.audioStreamClientUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Controller
public class PlayerController {

    private Player player;
    private SourceDataLine source;
    private UdpClient client;
    private ClientView view;
    private BlockingQueue<byte[]> queue;
    private Registrator registrator;

    private final AudioFormat FORMAT = new AudioFormat(44100f, 16, 2, true, false);

    @Autowired
    public PlayerController(Player player, UdpClient client, ClientView view, Registrator registrator) throws LineUnavailableException {
        this.registrator = registrator;
        this.view = view;
        this.player = player;
        this.client = client;
    }


    public void start() throws IOException {

        new Thread(registrator).start();
        new Thread(client).start();
        new Thread(player).start();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public UdpClient getClient() {
        return client;
    }

    public void setClient(UdpClient client) {
        this.client = client;
    }

    public ClientView getView() {
        return view;
    }

    public void setView(ClientView view) {
        this.view = view;
    }

    public Registrator getRegistrator() {
        return registrator;
    }

    public void setRegistrator(Registrator registrator) {
        this.registrator = registrator;
    }

    public void setLine() throws LineUnavailableException {
        source = AudioSystem.getSourceDataLine(FORMAT);
        player.setSource(source);
        source.open();
        source.start();
    }

    public void setControls() {

        BooleanControl mute = (BooleanControl) source.getControl(BooleanControl.Type.MUTE);

        view.getPlayButton().setOnAction(event -> { mute.setValue(!mute.getValue());
                                                    view.getPlayButton().setText(mute.getValue() ? ">" : "||"); });

        FloatControl volume = (FloatControl) source.getControl(FloatControl.Type.MASTER_GAIN);

        float slideUnit = (volume.getMaximum() - volume.getMinimum()) / 100;

        view.getVolSlider().setValue(volume.getValue() / slideUnit);
        view.getVolSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
            volume.setValue(volume.getMinimum() + newValue.floatValue() * slideUnit);}
            );
    }

    public void setInput() {
        queue = new LinkedBlockingQueue<>();
        client.setQueue(queue);
        player.setInput(queue);
    }

}
