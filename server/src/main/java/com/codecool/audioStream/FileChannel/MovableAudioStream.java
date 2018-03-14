package com.codecool.audioStream.FileChannel;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MovableAudioStream {


    private final File file;
    private AudioInputStream stream;

    public MovableAudioStream(File file) throws IOException, UnsupportedAudioFileException {
        this.file = file;
        this.stream = AudioSystem.getAudioInputStream(file);
    }

    public int read(byte[] buffer, int off, int len) throws IOException {
        return stream.read(buffer, off, len);
    }

    public long available() throws IOException {
        return stream.available();
    }

    public long skip(long n) throws IOException, UnsupportedAudioFileException {
        if (n > 0) {
            return stream.skip(n);
        } else if (n < 0) {
            long pos = stream.available();
            stream = AudioSystem.getAudioInputStream(file);
            stream.skip(stream.available() - pos + n);
            return n;
        } else {
            return 0;
        }
    }

    public AudioFormat getFormat() {
        return stream.getFormat();
    }
}

