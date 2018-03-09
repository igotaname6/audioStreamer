package com.codecool.audioStream;

import org.springframework.stereotype.Controller;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.net.InetAddress;
import java.util.concurrent.*;

@Controller
public class ServerController {

    private ConcurrentHashMap<InetAddress, Long> clientsConnected;
    private Broadcaster broadcaster;
    private ConnectionListener connectionListener;
    private UdpServer udpServer;
    private Streamer streamer;
    private BlockingQueue<byte[]> output;
    private TargetDataLine target;

    private final AudioFormat FORMAT = new AudioFormat(44100f, 16, 2, true, false);

    public ServerController(Broadcaster broadcaster, ConnectionListener connectionListener, UdpServer udpServer, Streamer streamer) {
        this.broadcaster = broadcaster;
        this.connectionListener = connectionListener;
        this.udpServer = udpServer;
        this.streamer = streamer;
    }

    public void start() {
        Executor executor = Executors.newFixedThreadPool(4);
        executor.execute(broadcaster);
        executor.execute(connectionListener);
        executor.execute(udpServer);
        executor.execute(streamer);
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
