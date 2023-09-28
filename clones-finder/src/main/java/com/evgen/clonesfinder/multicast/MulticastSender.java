package com.evgen.clonesfinder.multicast;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MulticastSender implements Runnable{
    private int port;
    private InetAddress addr;

    public MulticastSender(int port, InetAddress addr){
        this.addr = addr;
        this.port = port;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()){

            while (true){

                try {
                    String msg = "boo from " + new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));
                    byte[] bytes = msg.getBytes();

                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, addr, port);
                    socket.send(packet);

                    System.out.println("Sent: "+msg);

                    Thread.sleep(3000);
                } catch (IOException | InterruptedException e) {
                    System.out.println("Sender: "+e.getMessage());
                    System.exit(1);
                }

            }

        } catch (SocketException e){
            System.out.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }
}
