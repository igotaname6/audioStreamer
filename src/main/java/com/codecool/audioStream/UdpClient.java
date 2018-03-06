package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

@Service
public class UdpClient {

    private final String GROUP_IP = "230.0.0.1";
    private final int PORT = 4446;
    private final MulticastSocket socket;
    private boolean Finished;
    private final int bytesCount;

    public UdpClient(int bytesCount) throws IOException {
        this.bytesCount = bytesCount;
        socket = new MulticastSocket(PORT);
        socket.joinGroup(InetAddress.getByName(GROUP_IP));
    }

    public byte[] readBytes() throws IOException {
        byte[] data = new byte[bytesCount];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.receive(packet);
        return data;
    }

    public boolean isFinished() {
        return Finished;
    }

    public static void main(String[] args) throws IOException {
        int bytesCount = 256;
        UdpClient udpClient = new UdpClient(bytesCount);
        while (true){
            byte[] data = udpClient.readBytes();
            System.out.println(data[0]);
        }
    }

}
