package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

@Service
public class FilePlayer implements Channel {

    volatile BlockingQueue<byte[]> queue;
    volatile boolean onAir;
    volatile boolean fldbck;
    volatile boolean isOpened;
    volatile boolean isPlaying;
    volatile float masterGain;
    volatile SourceDataLine source;
    volatile AudioInputStream fileStream;

    @Override
    public void run() {

        try  {
            while (fileStream == null || !isPlaying) {}
            isOpened = true;
            byte[] buffer = new byte[(int)fileStream.getFormat().getFrameRate()];
            byte[] clone = new byte[buffer.length];
            byte[] silece = new byte[buffer.length];

            while (isOpened) {
                try {
                    if (isPlaying() && fileStream.read(buffer, 0, buffer.length) != -1) {
                        for (int i=0; i<buffer.length; i++) {
                            clone[i] = (byte) (buffer[i] * masterGain * (onAir? 1 : 0));
                        }
                        queue.put(clone);
                        //                        source.write(buffer, 0, buffer.length);
                    } else if (fileStream.available() > 0) {
                        queue.put(silece);
                    } else {
                        isOpened = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void setSource(SourceDataLine source) {
        this.source = source;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    @Override
    public void setMasterGain(float masterGain) {
        this.masterGain = masterGain;
    }

    @Override
    public void setOnAir(boolean onAir) {
        this.onAir = onAir;
    }

    @Override
    public void setFldbck(boolean fldbck) {
        this.fldbck = fldbck;
        ((BooleanControl) source.getControl(BooleanControl.Type.MUTE)).setValue(!fldbck);
    }

    @Override
    public boolean isOpen() {
        return isOpened;
    }

    public void setFileStream(AudioInputStream fileStream) {
        this.fileStream = fileStream;
    }

    @Override
    public boolean isOnAir() {
        return onAir;
    }

    @Override
    public boolean isFldbck() {
        return fldbck;
    }

    @Override
    public float getMasterGain() {
        return masterGain;
    }

    public AudioInputStream getFileStream() {
        return fileStream;
    }
}


