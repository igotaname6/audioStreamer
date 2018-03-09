package com.codecool.audioStream;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.util.concurrent.BlockingQueue;

@Service
public class Streamer implements Runnable {

    BlockingQueue<byte[]> queue;
    TargetDataLine target;

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
            System.out.println("line opened");
            target.read(buffer, 0, buffer.length);
            System.out.println(buffer[0]);
            try {
//                so.write(buffer, 0, buffer.length);
                queue.put(buffer.clone());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public BlockingQueue<byte[]> getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue<byte[]> queue) {
        this.queue = queue;
    }

    public TargetDataLine getTarget() {
        return target;
    }

    public void setTarget(TargetDataLine target) {
        this.target = target;
    }
}
