package com.codecool.audioStream;

import javax.sound.sampled.SourceDataLine;
import java.util.concurrent.BlockingQueue;

public interface Channel extends Runnable {

    @Override
    void run();

    void setQueue(BlockingQueue<byte[]> queue);
    BlockingQueue<byte[]> getQueue();

    float getMasterGain();
    void setMasterGain(float masterGain);

    boolean isOnAir();
    void setOnAir(boolean onAir);

    boolean isFldbck();
    void setFldbck(boolean fldbck);

    boolean isOpen();

    void close();

    BlockingQueue<byte[]> getFldbckQueue();
    void setFldbckQueue(BlockingQueue<byte[]> fldbckQueue);


}
