package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.concurrent.BlockingQueue;

@Service
public class FldbckPlayer implements Runnable {

    volatile SourceDataLine source;
    volatile BlockingQueue<byte[]> input;

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

    public FldbckPlayer setInput(BlockingQueue<byte[]> input) {
        this.input = input;
        return this;
    }

    public BlockingQueue<byte[]> getInput() {
        return input;
    }

    public SourceDataLine getSource() {
        return source;
    }

    public void setSource(SourceDataLine source) {
        this.source = source;
    }
}
