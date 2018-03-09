package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

@Service
public class FilePlayer {

    private SourceDataLine sourceLine;

    public void play(File file) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        AudioInputStream in = AudioSystem.getAudioInputStream(file);
        byte[] buffer = new byte[(int)in.getFormat().getFrameRate()];
        while (in.read(buffer, 0, buffer.length) != -1) {
            sourceLine.write(buffer.clone(), 0, buffer.length);
        }
    }

    public void setSourceLine(SourceDataLine sourceLine) {
        this.sourceLine = sourceLine;
    }
}


