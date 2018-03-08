package com.codecool.audioStreamClientUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Controller
public class PlayerController {

    private Player player;
    private UdpClient client;
    private ClientView view;
    private BlockingQueue<byte[]> queue;

    @Autowired
    public PlayerController(Player player, UdpClient client) {
        this.player = player;
        this.client = client;
        this.queue = new LinkedBlockingQueue<>();
    }

    public PlayerController() {
    }

    public void start() throws IOException {
        int bytesCount = 44100;

        this.player.setInput(this.queue).setFormat(new AudioFormat(44100f, 16, 2, true, false));

        Thread playerThread = new Thread(player);
        playerThread.start();

        while (true) {
            byte[] buff = client.readBytes();
            try {
                queue.put(buff);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public UdpClient getClient() {
        return client;
    }

    public void setClient(UdpClient client) {
        this.client = client;
    }

    public ClientView getView() {
        return view;
    }

    public void setView(ClientView view) {
        this.view = view;
    }
}
