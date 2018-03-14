package com.codecool.audioStream.FileChannel;

import com.codecool.audioStream.Channel;
import javafx.scene.control.Slider;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

@Service
public class FileChannel implements Channel {
    
    Slider progresBar;
    volatile BlockingQueue<byte[]> queue;
    volatile BlockingQueue<byte[]> fldbckQueue;
    volatile boolean onAir;
    volatile boolean fldbck;
    volatile boolean isOpened = true;
    volatile boolean isPlaying;
    volatile float masterGain;
    volatile MovableAudioStream fileStream;
    volatile private long position;

    @Override
    public void run() {

        try  {
            byte[] silece = new byte[44100];
            while (fileStream == null || !isPlaying) {
                try {
                    queue.put(silece);
                    fldbckQueue.put(silece);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            byte[] buffer = new byte[(int)fileStream.getFormat().getFrameRate()];
            byte[] clone = new byte[buffer.length];
            byte[] fldbckBuffer = new byte[buffer.length];

            while (isOpened) {
                try {
                    if (isPlaying() && fileStream.read(buffer, 0, buffer.length) != -1) {
                        int i=0;
                        for (; i<buffer.length; i++) {
                            clone[i] = (byte) (buffer[i] * masterGain * (onAir? 1 : 0));
                            fldbckBuffer[i] = (byte) (buffer[i] * masterGain * (fldbck? 1 : 0));
                        }
                        queue.put(clone);
                        fldbckQueue.put(fldbckBuffer);
                        position += 44100;
                        progresBar.setValue(position);
                    } else if (fileStream.available() > 0) {
                        queue.put(silece);
                        fldbckQueue.put(silece);
                    } else {
                        System.out.println("koniec");
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
    }

    @Override
    public boolean isOpen() {
        return isOpened;
    }

    @Override
    public void close() {
        isOpened = false;
    }

    public void setFileStream(File file) throws IOException, UnsupportedAudioFileException {
        this.fileStream = new MovableAudioStream(file);
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

    public MovableAudioStream getFileStream() {
        return fileStream;
    }

    public void setPosition(long position) {
        try {
            fileStream.skip(position - this.position);
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        this.position = position;
    }

    public void setProgresBar(Slider progresBar) {
        this.progresBar = progresBar;
    }
}


