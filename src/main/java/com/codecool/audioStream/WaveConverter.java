package com.codecool.audioStream;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class WaveConverter {

    private FileInputStream inputStream;
    private int bytesCount;
    private boolean hasBytes;

    public WaveConverter(String filePath, int bytesCount) throws IOException {
        this.inputStream = new FileInputStream(filePath);
        this.bytesCount = bytesCount;
        hasBytes = inputStream.available() > 0;
    }

    public boolean hasBytes() throws IOException {
        return inputStream.available() > 0;
    }

    public byte[] getNextBytes() throws IOException {
        byte[] data = new byte[bytesCount];
        int bytesRead = inputStream.read(data);
        return data;
    }
}
