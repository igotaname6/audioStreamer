package com.codecool.audioStream;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class UdpServer {

    private final String GROUP_IP = "192.168.11.7";
    private final int PORT = 4446;
    private final DatagramSocket socket;

    private InputStream in;

    public UdpServer(InputStream file) throws IOException {
        this.in = file;
        this.socket = new DatagramSocket(4445);
    }

    public void start() throws IOException, LineUnavailableException {

        AudioFormat af = new AudioFormat(44100f, 16, 2, true, false);

        SourceDataLine sp = AudioSystem.getSourceDataLine(af);
        sp.open();
        sp.start();
        ((BooleanControl) sp.getControl(BooleanControl.Type.MUTE)).setValue(true);

        byte[] buffer = new byte[44100];
        int counter = 0;
        int sum = 0;
        while(in.read(buffer, 0, 44100) != -1) {

            try {
                sp.write(buffer.clone(), 0, 44100);
                InetAddress groupAddress = InetAddress.getByName(GROUP_IP);
//                byte[] data = converter.getNextBytes();
//                System.out.println(buffer.clone());
                sum += buffer.clone()[0];
                counter++;
                System.out.println(counter + ", suma: " + sum);
                DatagramPacket packet = new DatagramPacket(buffer.clone(),  buffer.length, groupAddress, PORT);
                socket.send(packet);
//                Thread.sleep(200);
            } catch (IOException e) {
                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
        }
        socket.close();
    }

    public static void main(String[] args) throws IOException, LineUnavailableException {
        while (true) {
            String path = "resources/sample.wav";
//            int bytesCount = 44100;
//            WaveConverter waveConverter = new WaveConverter(path, bytesCount);
            UdpServer udpServer = new UdpServer(new FileInputStream(new File(path)));
            udpServer.start();
        }
    }
}
