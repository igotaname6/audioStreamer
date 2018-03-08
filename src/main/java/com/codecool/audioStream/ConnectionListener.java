package com.codecool.audioStream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentSkipListSet;

public class ConnectionListener implements Runnable{

    private ConcurrentSkipListSet<SocketAddress> set;

    private final int LISTENING_PORT = 4445;
    private boolean running;
    private DatagramSocket socket;

    public ConnectionListener(ConcurrentSkipListSet set) {
        this.set = set;
    }

    @Override
    public void run() {
        running = true;
        try {
            socket = new DatagramSocket(LISTENING_PORT);
            while (running){
                byte[] bf = new byte[1];
                DatagramPacket packet = new DatagramPacket(bf, bf.length);
                socket.receive(packet);
                set.add(packet.getSocketAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
