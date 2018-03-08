package com.codecool.audioStream;

import java.io.IOException;
import java.net.*;

public class Broadcaster implements Runnable{

    private final String BROADCAST_IP = "192.168.11.255";
    private final int BROADCAST_PORT = 4446;
//    private final int LISTENING_PORT = 4445;
    private boolean running;
    private DatagramSocket socket;

    @Override
    public void run() {
        try {
            socket = new DatagramSocket();
            running= true;
            while (running){
                byte[] bt = {0};
                InetAddress broadcastingIp = InetAddress.getByName(BROADCAST_IP);
                DatagramPacket packet = new DatagramPacket(bt, bt.length, broadcastingIp, BROADCAST_PORT);
                socket.send(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
