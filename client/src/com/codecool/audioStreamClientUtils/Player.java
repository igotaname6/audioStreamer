package com.codecool.audioStreamClientUtils;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.concurrent.BlockingQueue;

@Service
public class Player implements Runnable {

    AudioFormat format;
    BlockingQueue<byte[]> input;

    public Player() {
    }

    public AudioFormat getFormat() {
        return format;
    }

    public Player setFormat(AudioFormat format) {
        this.format = format;
        return this;
    }

    public BlockingQueue<byte[]> getInput() {
        return input;
    }

    public Player setInput(BlockingQueue<byte[]> input) {
        this.input = input;
        return this;
    }

    @Override
    public void run() {

        try {
            SourceDataLine out = AudioSystem.getSourceDataLine(format);
            out.open(format);
//            Line.Info info = out.getLineInfo();
//            Arrays.stream(AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]).getSourceLines()).forEach(System.out::println);
            out.start();
//            Arrays.stream(out.getControls()).forEach(System.out::println);
//            BooleanControl muter = (BooleanControl)out.getControl(BooleanControl.Type.MUTE);
//            FloatControl vol = (FloatControl)out.getControl(FloatControl.Type.MASTER_GAIN);
//            System.out.println(input.size());
            while (out.isOpen()) {
//                if (vol.getValue() > vol.getMinimum())
//                    vol.setValue(vol.getValue() - 0.5f);
                out.write(input.take(), 0, (int) format.getFrameRate());
//                System.out.println(input.size());
            }

        } catch (LineUnavailableException | InterruptedException e ) {
            e.printStackTrace();
        }

    }
}
