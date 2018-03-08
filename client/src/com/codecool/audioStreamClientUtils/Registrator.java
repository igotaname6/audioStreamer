package com.codecool.audioStreamClientUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Registrator {

    private final int LISTENING_PORT  = 4446;
    private final String BROADCASTING_IP = "192.168.11.255";

    public void establishConnection() throws IOException {
        DatagramSocket socket = new DatagramSocket(LISTENING_PORT);
        byte[] bf = new byte[1];
        DatagramPacket packet = new DatagramPacket(bf, bf.length);

        socket.receive(packet);
        packet.setAddress(packet.getAddress());
        packet.setPort(4445);
        System.out.println(packet.getAddress() + " " + packet.getPort());
        socket.send(packet);
    }

    public static void main(String[] args) {
        Registrator registrator = new Registrator();
        try {
            registrator.establishConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
