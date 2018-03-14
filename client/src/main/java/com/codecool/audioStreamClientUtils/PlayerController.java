package com.codecool.audioStreamClientUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.concurrent.*;

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
    public PlayerController(Player player, UdpClient client, ClientView view, Registrator registrator) {
        this.registrator = registrator;
        this.view = view;
        this.player = player;
        this.client = client;
    }


    public void start() {
        Executor executor = Executors.newCachedThreadPool();

        executor.execute(registrator);
        executor.execute(client);
        executor.execute(player);
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

        view.getVolSlider().setMin(volume.getMinimum());
        view.getVolSlider().setMax(volume.getMaximum());

        view.getVolSlider().setValue(volume.getValue());
        view.getVolSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
            volume.setValue(newValue.floatValue());}
            );
    }

    public void setInput() {
        queue = new LinkedBlockingQueue<>();
        client.setQueue(queue);
        player.setInput(queue);
    }

}
