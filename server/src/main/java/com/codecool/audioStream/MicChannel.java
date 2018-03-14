package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

@Service
public class MicChannel implements Channel {

    volatile BlockingQueue<byte[]> queue;
    volatile boolean onAir;
    volatile boolean fldbck;
    volatile boolean isOpened = true;
    volatile float masterGain;
    volatile TargetDataLine target;
    private BlockingQueue<byte[]> fldbckQueue;


    @Override
    public void run() {

        byte[] buffer = new byte[(int) target.getFormat().getFrameRate()];
        byte[] clone = new byte[buffer.length];
        byte[] fldbckBuffer = new byte[buffer.length];

        while (isOpened) {
            try {
                if (target.read(buffer, 0, buffer.length) != -1) {
                    for (int i=0; i<buffer.length; i++) {
                        clone[i] = (byte) (buffer[i] * masterGain * (onAir? 1 : 0));
                        fldbckBuffer[i] = (byte) (buffer[i] * masterGain * (fldbck? 1 : 0));
                    }
                }
                queue.put(clone);
                fldbckQueue.put(fldbckBuffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        target.stop();
        target.close();
    }

    @Override
    public void setQueue(BlockingQueue<byte[]> queue) {
        this.queue = queue;
    }

    @Override
    public BlockingQueue<byte[]> getQueue() {
        return queue;
    }

    @Override
    public BlockingQueue<byte[]> getFldbckQueue() {
        return fldbckQueue;
    }

    @Override
    public void setFldbckQueue(BlockingQueue<byte[]> fldbckQueue) {
        this.fldbckQueue = fldbckQueue;
    }

    @Override
    public float getMasterGain() {
        return masterGain;
    }

    @Override
    public void setMasterGain(float masterGain) {
        this.masterGain = masterGain;

    }

    @Override
    public boolean isOnAir() {
        return onAir;
    }

    @Override
    public void setOnAir(boolean onAir) {
        this.onAir = onAir;

    }

    @Override
    public boolean isFldbck() {
        return fldbck;
    }

    @Override
    public void setFldbck(boolean fldbck) {
        this.fldbck = fldbck;
    }

    @Override
    public boolean isOpen() {
        return isOpened;
    }

    @Override
    public void close() {
        isOpened = false;
    }

    public void setTarget(TargetDataLine target) {
        this.target = target;
    }
}
