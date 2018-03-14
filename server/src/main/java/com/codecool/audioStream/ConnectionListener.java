package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class ConnectionListener implements Runnable {

    private ConcurrentHashMap<InetAddress, Long> clientsMap;

    private final int LISTENING_PORT = 4446;

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(LISTENING_PORT);
            while (true){
                byte[] bf = new byte[1];
                DatagramPacket packet = new DatagramPacket(bf, bf.length);
                socket.receive(packet);
                clientsMap.put(packet.getAddress(), System.currentTimeMillis());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConcurrentHashMap<InetAddress, Long> getClientsMap() {
        return clientsMap;
    }

    public void setClientsMap(ConcurrentHashMap<InetAddress, Long> clientsMap) {
        this.clientsMap = clientsMap;
    }
}
