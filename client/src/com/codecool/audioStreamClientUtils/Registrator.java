package com.codecool.audioStreamClientUtils;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

@Service
public class Registrator implements Runnable {

    private final int LISTENING_PORT  = 4445;
    private final int REGISTER_PORT = 4446;
    private final String BROADCASTING_IP = "192.168.11.255";

    private void register(DatagramSocket socket) throws IOException {

        byte[] bf = new byte[1];
        DatagramPacket packet = new DatagramPacket(bf, bf.length);
        socket.receive(packet);
        System.out.println(packet.toString());
        packet.setAddress(packet.getAddress());
        packet.setPort(REGISTER_PORT);
        System.out.println(packet.getAddress() + " " + packet.getPort());
        socket.send(packet);
    }

    @Override
    public void run() {

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(LISTENING_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                register(socket);
                Thread.sleep(20000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
