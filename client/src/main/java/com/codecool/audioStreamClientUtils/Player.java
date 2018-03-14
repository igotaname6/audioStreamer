package com.codecool.audioStreamClientUtils;

import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.util.concurrent.BlockingQueue;

@Service
public class Player implements Runnable {

    private SourceDataLine source;
    private BlockingQueue<byte[]> input;

    public BlockingQueue<byte[]> getInput() {
        return input;
    }

    public Player setInput(BlockingQueue<byte[]> input) {
        this.input = input;
        return this;
    }

    public SourceDataLine getSource() {
        return source;
    }

    public void setSource(SourceDataLine source) {
        this.source = source;
    }

    @Override
    public void run() {

        try {
            while (source.isOpen()) {
                byte[] frame = input.take();
                source.write(frame, 0, frame.length);
            }

        } catch (InterruptedException e ) {
            e.printStackTrace();
        }

    }
}
