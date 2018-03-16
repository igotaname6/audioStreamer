package com.codecool.audioStream.FileChannel;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class BiDirectionalTimer {

    long elapsedSeconds;
//    long playSeconds = 0;

//    public void setFileLength(long fileLength) {
//        this.elapsedSeconds = fileLength;
//        System.out.println(getElapsedString());
//    }

    public String getElapsedString() {
        return LocalTime.ofSecondOfDay(elapsedSeconds).format(DateTimeFormatter.ofPattern("H:mm:ss"));
    }

//    public String getPlayString() {
//        return LocalTime.ofSecondOfDay(playSeconds).format(DateTimeFormatter.ofPattern("H:mm:ss"));
//    }


    public void setElapsed(float elapsed) {
        this.elapsedSeconds = (elapsed > 0)? (long) elapsed : 0;
    }
}
