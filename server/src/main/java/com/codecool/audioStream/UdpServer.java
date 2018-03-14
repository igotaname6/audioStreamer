package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UdpServer implements Runnable {

    private final int PORT = 4447;
    private BlockingQueue<byte[]> queue;
    private ConcurrentHashMap<InetAddress, Long> clientsConnected;


    @Override
    public void run() {

        try (DatagramSocket socket = new DatagramSocket()) {

            while(true) {

                final byte[] buffer = queue.take();
                clientsConnected.forEach((ip, timestamp) -> {
                    try {
                        if ((System.currentTimeMillis() - timestamp > 60000)) {
                            clientsConnected.remove(ip);
                        } else {
                            socket.send(new DatagramPacket(buffer,0, buffer.length, ip, PORT));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setQueue(BlockingQueue<byte[]> queue) { this.queue = queue; }

    public ConcurrentHashMap<InetAddress, Long> getClientsConnected() {
        return clientsConnected;
    }

    public void setClientsConnected(ConcurrentHashMap<InetAddress, Long> clientsConnected) {
        this.clientsConnected = clientsConnected;
    }
}
