package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.util.concurrent.BlockingQueue;

@Service
public class Streamer implements StreamerInterface {

    private BlockingQueue<byte[]> queue;
    private TargetDataLine target;

    @Override
    public void run() {
        SourceDataLine so = null;
        try {
            so = AudioSystem.getSourceDataLine(new AudioFormat(44100f, 16, 2, true, false));
            so.open();
            so.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        byte[] buffer = new byte[(int) target.getFormat().getFrameRate()];

        while (target.isOpen()) {
            target.read(buffer, 0, buffer.length);
            try {
//                so.write(buffer, 0, buffer.length);
                queue.put(buffer.clone());
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public BlockingQueue<byte[]> getQueue() {
        return queue;
    }

    @Override
    public void setQueue(BlockingQueue<byte[]> queue) {
        this.queue = queue;
    }

    public TargetDataLine getTarget() {
        return target;
    }

    @Override
    public void setTarget(TargetDataLine target) {
        this.target = target;
    }
}
