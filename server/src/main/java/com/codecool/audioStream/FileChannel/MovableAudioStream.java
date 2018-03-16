package com.codecool.audioStream.FileChannel;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MovableAudioStream {


    private final File file;
    private AudioInputStream stream;
    float totalTime;

    public MovableAudioStream(File file) throws IOException, UnsupportedAudioFileException {
        this.file = file;
        this.stream = AudioSystem.getAudioInputStream(file);
        totalTime = getElapsedTime();
    }

    public int read(byte[] buffer, int off, int len) throws IOException {
        return stream.read(buffer, off, len);
    }

    public long available() throws IOException {
        return stream.available();
    }

    public long skip(long n) throws IOException, UnsupportedAudioFileException {
        long pos = stream.available();
//        if (Math.abs(n) > file.length()) {
//            return (n > 0) ? file.length() - pos : -pos;
        if (n > 0) {
            return (n > pos)? stream.skip(pos) : stream.skip(n);
        } else if (n < 0) {
            stream = AudioSystem.getAudioInputStream(file);
            if (n < pos -file.length()) {
                return pos - file.length();
//            }
//            if (pos + n < 0) {
//                return -pos;
            } else {
                stream.skip(stream.available() - pos + n);
                return n;
            }
        } else {
            return 0;
        }
    }

    public AudioFormat getFormat() {
        return stream.getFormat();
    }

    public float getElapsedTime() throws IOException {
        return stream.available() / (getFormat().getFrameSize() * getFormat().getFrameRate());
    }

    public long getOriginalLen() {
        return file.length();
    }

    public void close() throws IOException {
        stream.close();
    }
}

