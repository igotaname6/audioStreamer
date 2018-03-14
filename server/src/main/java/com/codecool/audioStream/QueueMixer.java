package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@Service
public class QueueMixer implements Runnable {

    volatile BlockingQueue<byte[]> output;
    volatile BlockingQueue<byte[]> fldbck;
    volatile List<Channel> inputs = new LinkedList<>();

    public void setOutput(BlockingQueue<byte[]> output) {
        this.output = output;
    }

    public void setFldbck(BlockingQueue<byte[]> fldbck) {
        this.fldbck = fldbck;
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
        byte[] fldbckBuffer;
        byte[] channelframe;
        byte[] channelFldbck;

        while (!inputs.isEmpty()) {

            frame = new byte[44100];
            fldbckBuffer = new byte[frame.length];
            int size = inputs.size();
            for (int i=0; i<size; i++) {
                try {
                    if (inputs.get(i).isOpen()) {
                        channelframe = inputs.get(i).getQueue().take();
                        channelFldbck = inputs.get(i).getFldbckQueue().take();
                        for (int j=0; j<frame.length; j++) {
                            frame[j] = (byte) (frame[j] + channelframe[j]);
                            fldbckBuffer[j] = (byte) (fldbckBuffer[j] + channelFldbck[j]);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            inputs = inputs.stream().filter(Channel::isOpen).collect(Collectors.toList());
            try {
                output.put(frame);
                fldbck.put(fldbckBuffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
