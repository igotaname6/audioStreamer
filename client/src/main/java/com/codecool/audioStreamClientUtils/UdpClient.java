package com.codecool.audioStreamClientUtils;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.BlockingQueue;

@Service
public class UdpClient implements Runnable {

    private final int PORT = 4447;
    private final DatagramSocket socket;
    private boolean Finished;
    private final int bytesCount;
    private BlockingQueue<byte[]> queue;


    public UdpClient() throws IOException {
        this.bytesCount = 44100;
        socket = new DatagramSocket(PORT);
    }

    public void setQueue(BlockingQueue<byte[]> queue) {
        this.queue = queue;
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

    @Override
    public void run() {

        while (true) {
            try {
                queue.put(readBytes());
                System.out.println(">");
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
