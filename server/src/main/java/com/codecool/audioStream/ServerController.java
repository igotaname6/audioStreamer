package com.codecool.audioStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.concurrent.*;

@Controller
public class ServerController {

    private ConcurrentHashMap<InetAddress, Long> clientsConnected;
    private Broadcaster broadcaster;
    private ConnectionListener connectionListener;
    private UdpServer udpServer;
    private FilePlayer player;
    private Streamer streamer;
    private BlockingQueue<byte[]> output;
    private TargetDataLine target;
    private FilePlayer filePlayer;
    private SourceDataLine source;
    private Mixer mixer;

    private final AudioFormat FORMAT = new AudioFormat(44100f, 16, 2, true, false);

    @Autowired
    public ServerController(FilePlayer filePlayer, Broadcaster broadcaster, ConnectionListener connectionListener, UdpServer udpServer, Streamer streamer) {
        this.broadcaster = broadcaster;
        this.connectionListener = connectionListener;
        this.udpServer = udpServer;
        this.streamer = streamer;
        this.filePlayer = filePlayer;
    }

    public void start() {

        Executor executor = Executors.newFixedThreadPool(4);
        executor.execute(broadcaster);
        executor.execute(connectionListener);
        executor.execute(udpServer);

        Scanner input = new Scanner(System.in);

        while (true) {
            switch (input.nextLine()) {
                case "file":
                    executor.execute(filePlayer);
                    break;
                case "mic":
                    executor.execute(streamer);
                    break;
                default:
                    System.out.println("wrong input");
                    break;
            }
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
        filePlayer.setQueue(output);
        udpServer.setQueue(output);
    }

    public void setLine() throws LineUnavailableException {
        target = AudioSystem.getTargetDataLine(FORMAT);
        streamer.setTarget(target);
        filePlayer.setTarget(target);
        target.open();
        target.start();
    }

}