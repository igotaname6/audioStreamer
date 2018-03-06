package com.codecool.audioStream;

import javax.sound.sampled.*;
import java.util.Arrays;
import java.util.Queue;

public class Player implements Runnable {

    AudioFormat format;
    Queue<byte[]> input;

    public AudioFormat getFormat() {
        return format;
    }

    public Player setFormat(AudioFormat format) {
        this.format = format;
        return this;
    }

    public Queue<byte[]> getInput() {
        return input;
    }

    public Player setInput(Queue<byte[]> input) {
        this.input = input;
        return this;
    }

    @Override
    public void run() {

        try {
            SourceDataLine out = AudioSystem.getSourceDataLine(format);
            out.open(format);
            Line.Info info = out.getLineInfo();
//            Arrays.stream(AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]).getSourceLines()).forEach(System.out::println);
            out.start();
            Arrays.stream(out.getControls()).forEach(System.out::println);
            BooleanControl muter = (BooleanControl)out.getControl(BooleanControl.Type.MUTE);
            FloatControl vol = (FloatControl)out.getControl(FloatControl.Type.MASTER_GAIN);
            System.out.println(input.size());
            while (!input.isEmpty()) {
//                if (vol.getValue() > vol.getMinimum())
//                    vol.setValue(vol.getValue() - 0.5f);
                out.write(input.poll(), 0, (int) format.getFrameRate());
                System.out.println(input.size());
            }

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }
}
