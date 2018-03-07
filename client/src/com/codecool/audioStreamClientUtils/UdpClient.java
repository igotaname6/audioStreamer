package com.codecool.audioStreamClientUtils;

import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class UdpClient {

    private final String GROUP_IP = "230.0.0.1";
    private final int PORT = 4446;
    private final MulticastSocket socket;
    private boolean Finished;
    private final int bytesCount;

    public UdpClient(int bytesCount) throws IOException {
        this.bytesCount = bytesCount;
        socket = new MulticastSocket(PORT);
        socket.joinGroup(InetAddress.getByName(GROUP_IP));
    }

    public byte[] readBytes() throws IOException {
        byte[] data = new byte[bytesCount];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.receive(packet);
        return data;
    }

    public boolean isFinished() {
        return Finished;
    }

    public static void main(String[] args) throws IOException {
        int bytesCount = 44100;
        UdpClient udpClient = new UdpClient(bytesCount);

        BlockingQueue<byte[]> queue = new LinkedBlockingQueue<byte[]>();

        new Thread(new Player().setInput(queue).setFormat(new AudioFormat(44100f, 16, 2, true, false))).start();

        while (true) {
            byte[] buff = udpClient.readBytes();
            try {
                queue.put(buff);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Player implements Runnable {

        AudioFormat format;
        BlockingQueue<byte[]> input;

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
}
