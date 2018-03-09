package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

@Service
public class FilePlayer implements StreamerInterface {

    private BlockingQueue<byte[]> queue;
    private TargetDataLine targetDataLine;
    private File file = new File(getClass().getClassLoader().getResource("msJackson.wav").getFile());

    @Override
    public void setQueue(BlockingQueue<byte[]> queue) {
        this.queue = queue;
    }

    @Override
    public void setTarget(TargetDataLine target) {
        this.targetDataLine = target;

    }



    @Override
    public void run() {

        SourceDataLine sdl = null;
        try {
            sdl = AudioSystem.getSourceDataLine(new AudioFormat(44100f, 16, 2, true, false));
            sdl.open();
            sdl.start();
            ((BooleanControl)sdl.getControl(BooleanControl.Type.MUTE)).setValue(true);

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        AudioInputStream in = null;
        try {
            in = AudioSystem.getAudioInputStream(file);
            byte[] buffer = new byte[(int)in.getFormat().getFrameRate()];
            while (in.read(buffer, 0, buffer.length) != -1) {
                try {
                    sdl.write(buffer.clone(), 0, buffer.length);
                    queue.put(buffer.clone());
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }
}


