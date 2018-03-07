package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
        int bytesCount = 44100;
        UdpClient udpClient = new UdpClient(bytesCount);

        BlockingQueue<byte[]> queue = new LinkedBlockingQueue<byte[]>() {
        };

        new Thread(new Player().setInput(queue).setFormat(new AudioFormat(44100f, 16, 2, true, false))).start();

        while (true) {
            byte[] buff = udpClient.readBytes();
            try {
                queue.put(buff);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println(buff.length + " > " + buff[0]);
        }


    }

}
