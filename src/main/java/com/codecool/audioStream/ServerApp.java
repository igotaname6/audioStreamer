package com.codecool.audioStream;


import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sound.sampled.LineUnavailableException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ServerApp {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext("com.codecool.audioStream");
        UdpServer udpServer = (UdpServer) context.getBean("UdpServer");
        try {
            InputStream in = new FileInputStream("/resources/sample.wav");
            udpServer.setIn(in);
            udpServer.start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }
}
