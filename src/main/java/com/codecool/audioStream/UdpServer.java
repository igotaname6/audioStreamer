package com.codecool.audioStream;

import java.io.IOException;
import java.net.*;

public class UdpServer {

    private final String GROUP_IP = "230.0.0.1";
    private final int PORT = 4446;
    private final DatagramSocket socket;

    private WaveConverter converter;

    public UdpServer(WaveConverter converter) throws IOException {
        this.converter = converter;
        this.socket = new DatagramSocket(4445);
    }

    public void start() throws IOException {

        while(converter.hasBytes()){
            try {
                InetAddress groupAddress = InetAddress.getByName(GROUP_IP);
                byte[] data = converter.getNextBytes();
                System.out.println(data);
                DatagramPacket packet = new DatagramPacket(data, data.length, groupAddress, PORT);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        String path = "/home/igotaname/Desktop/sample.wav";
        int bytesCount = 256;
        WaveConverter waveConverter = new WaveConverter(path, bytesCount);
        UdpServer udpServer = new UdpServer(waveConverter);
        udpServer.start();
    }
}
