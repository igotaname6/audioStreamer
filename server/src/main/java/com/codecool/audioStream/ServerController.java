package com.codecool.audioStream;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@Controller
public class ServerController {

    private QueueMixer mixer;
    private Broadcaster broadcaster;
    private ConnectionListener connectionListener;
    private UdpServer udpServer;
    private MicChannelView view;
    private MicChannel micChannel;

    private BlockingQueue<byte[]> output;
    private TargetDataLine target;
    private SourceDataLine source;
    private ConcurrentHashMap<InetAddress, Long> clientsConnected;

    private final AudioFormat FORMAT = new AudioFormat(44100f, 16, 2, true, false);

    @Autowired
    public ServerController(QueueMixer mixer, Broadcaster broadcaster,
                            ConnectionListener connectionListener, UdpServer udpServer,
                            MicChannelView view, MicChannel micChannel) {
        this.broadcaster = broadcaster;
        this.connectionListener = connectionListener;
        this.udpServer = udpServer;
        this.mixer = mixer;
        this.view = view;
        this.micChannel = micChannel;
    }

    public void start() {

        Executor executor = Executors.newFixedThreadPool(5);
        executor.execute(broadcaster);
        executor.execute(connectionListener);
        executor.execute(mixer);
        executor.execute(udpServer);
    }

    public void setClientsMap () {
        clientsConnected = new ConcurrentHashMap<>();
        connectionListener.setClientsMap(clientsConnected);
        udpServer.setClientsConnected(clientsConnected);
    }

    public void setMixer () throws LineUnavailableException {
        output = new LinkedBlockingQueue<>();
        mixer.setOutput(output);
        udpServer.setQueue(output);
        setMicLine();
        setFoldback();
    }

    public void setMicLine() throws LineUnavailableException{
        target = AudioSystem.getTargetDataLine(FORMAT);
        target.open();
        target.start();
    }

    public void setMicChannel()  {
        micChannel.setTarget(target);
        micChannel.setSource(source);
        micChannel.setQueue(new LinkedBlockingQueue<>());
        mixer.addChannel(micChannel);
        MicChannelController controller = new MicChannelController(view, micChannel);
        controller.start();

    }

    public void setWindow(Stage stage) throws IOException {
        view.setStage(stage);
    }

    public void setFoldback() throws LineUnavailableException {
        source = AudioSystem.getSourceDataLine(FORMAT);
        source.open();
        source.start();
    }

    public void addFileChannel() {
        FilePlayer player = new FilePlayer();
        FileChannelController channelController = new FileChannelController(new FileChannelView(), player);
        player.setSource(source);
        player.setQueue(new LinkedBlockingQueue<>(3));
        mixer.addChannel(player);
        try {
            channelController.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MicChannelController {

        private MicChannelView view;
        private MicChannel channel;

        public MicChannelController(MicChannelView view, MicChannel micChannel) {
            this.view = view;
            this.channel = micChannel;
        }

        public void start() {
            channel.setMasterGain(0);

            offAir();
            offFldbck();

            view.getOnAir().setOnAction(event -> {if (channel.isOnAir()) offAir(); else onAir();});
            view.getFldbck().setOnAction(event -> {if (channel.isFldbck()) offFldbck(); else onFldbck();});

            view.getAddChannel().setOnAction(event -> addFileChannel());

            view.getGain().setMin(0);
            view.getGain().setMax(1);
            view.getGain().setValue(channel.getMasterGain());
            view.getGain().valueProperty().addListener((observable, oldValue, newValue) -> channel.setMasterGain(newValue.floatValue()));
            view.showWindow();
            new Thread(channel).start();
        }

        public void onAir() {
            channel.setOnAir(true);
            view.setPressedOnAirButton();
        }

        public void offAir() {
            channel.setOnAir(false);
            view.setReleasedOnAirButton();
        }

        public void onFldbck() {
            channel.setFldbck(true);
            view.setPressedFldbckButton();
        }

        public void offFldbck() {
            channel.setFldbck(false);
            view.setReleasedFldbckButton();
        }
    }



}