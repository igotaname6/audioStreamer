package com.codecool.audioStream;

import org.springframework.stereotype.Controller;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Controller
public class ServerController {

    ConcurrentHashMap<InetAddress, Long> clientsConnected;
    Broadcaster broadcaster;
    ConnectionListener connectionListener;
    UdpServer udpServer;
    Streamer streamer;
    FilePlayer filePlayer;
    BlockingQueue<byte[]> output;
    TargetDataLine target;
    SourceDataLine source;

    private final AudioFormat FORMAT = new AudioFormat(44100f, 16, 2, true, false);

    public ServerController(FilePlayer filePlayer, Broadcaster broadcaster, ConnectionListener connectionListener, UdpServer udpServer, Streamer streamer) {
        this.broadcaster = broadcaster;
        this.connectionListener = connectionListener;
        this.udpServer = udpServer;
        this.streamer = streamer;
        this.filePlayer = filePlayer;
    }

    public void start() {
        new Thread(broadcaster).start();
        new Thread(connectionListener).start();
        new Thread(udpServer).start();
        new Thread(streamer).start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            filePlayer.setSourceLine(AudioSystem.getSourceDataLine(FORMAT));

            filePlayer.play(new File(getClass().getClassLoader().getResource("sample.wav").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public void setClientsMap () {
        clientsConnected = new ConcurrentHashMap<>();
        connectionListener.setClientsMap(clientsConnected);
        udpServer.setClientsConnected(clientsConnected);
    }

    public void setOutput () {
        output = new LinkedBlockingQueue<>();
        streamer.setQueue(output);
        udpServer.setQueue(output);
    }

    public void setLine() throws LineUnavailableException {
        target = AudioSystem.getTargetDataLine(FORMAT);
        streamer.setTarget(target);
        target.open();
        target.start();
    }
}
