package com.codecool.audioStream;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UdpServer implements Runnable {

    private final int PORT = 4447;
    private BlockingQueue<byte[]> queue;
    private ConcurrentHashMap<InetAddress, Long> clientsConnected;


    @Override
    public void run() {
        new AudioFormat(44100f, 16, 2, true, false);

        try (DatagramSocket socket = new DatagramSocket(PORT)) {

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
