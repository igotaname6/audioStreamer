package com.codecool.audioStream;

import org.springframework.stereotype.Controller;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.net.InetAddress;
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
    BlockingQueue<byte[]> output;
    TargetDataLine target;

    private final AudioFormat FORMAT = new AudioFormat(44100f, 16, 2, true, false);

    public ServerController(Broadcaster broadcaster, ConnectionListener connectionListener, UdpServer udpServer, Streamer streamer) {
        this.broadcaster = broadcaster;
        this.connectionListener = connectionListener;
        this.udpServer = udpServer;
        this.streamer = streamer;
    }

    public void start() {
        new Thread(broadcaster).start();
        new Thread(connectionListener).start();
        new Thread(udpServer).start();
        new Thread(streamer).start();
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
