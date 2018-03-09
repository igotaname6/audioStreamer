package com.codecool.audioStream;

import javax.sound.sampled.TargetDataLine;
import java.util.concurrent.BlockingQueue;

public interface StreamerInterface extends Runnable {
    void setQueue(BlockingQueue<byte[]> queue);

    void setTarget(TargetDataLine target);
}
