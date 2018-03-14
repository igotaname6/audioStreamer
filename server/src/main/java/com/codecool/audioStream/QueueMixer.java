package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Service
public class QueueMixer implements Runnable {

    volatile BlockingQueue<byte[]> output;
    volatile List<Channel> inputs = new LinkedList<>();

    public void setOutput(BlockingQueue<byte[]> output) {
        this.output = output;
    }

    public void addChannel(Channel channel) {
        inputs.add(channel);
    }

    public void removeChannel(Channel channel) {
        inputs.remove(channel);
    }

    @Override
    public void run() {

        while (inputs.isEmpty()){}

        byte[] frame;
        byte[] channelframe;

        while (!inputs.isEmpty()) {

            frame = new byte[44100];
            int size = inputs.size();
            for (int i=0; i<size; i++) {
                try {
                    if (inputs.get(i).isOpen()) {
                        channelframe = inputs.get(i).getQueue().take();
                        for (int j=0; j<frame.length; j++) {
                            frame[j] = (byte) (frame[j] + channelframe[j]);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                output.put(frame);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
