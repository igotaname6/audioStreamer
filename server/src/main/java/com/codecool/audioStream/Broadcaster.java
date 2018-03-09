package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class Broadcaster implements Runnable {

    private final String BROADCAST_IP = "192.168.11.255";
    private final int BROADCAST_PORT = 4445;

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket();
            while (true) {
                byte[] bt = {0};
                InetAddress broadcastingIp = InetAddress.getByName(BROADCAST_IP);
                DatagramPacket packet = new DatagramPacket(bt, bt.length, broadcastingIp, BROADCAST_PORT);
                socket.send(packet);
                try {
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
