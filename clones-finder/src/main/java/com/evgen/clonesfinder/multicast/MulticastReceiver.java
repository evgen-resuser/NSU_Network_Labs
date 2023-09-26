package com.evgen.clonesfinder.multicast;

import com.evgen.clonesfinder.misc.ClientData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver implements Runnable{

    private final MulticastHandler handler;
    private final InetAddress addr;
    private final int port;

    public MulticastReceiver(MulticastHandler handler, InetAddress addr, int port) {
        this.handler = handler;
        this.addr = addr;
        this.port = port;
    }

    private final byte[] bytes = new byte[256];

    @Override
    public void run() {
        try (MulticastSocket socket = new MulticastSocket(port)){

            socket.joinGroup(addr);

            while (true){
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                socket.receive(packet);

                String msg = new String(packet.getData(), 0, packet.getLength());
                //System.out.println("rec: "+msg);

                String[] words = msg.split(" ");
                if (words[0].equals("boo")){
                    ClientData newClient = new ClientData(packet.getAddress(), packet.getPort());
                    handler.addClient(newClient);
                    //handler.printClients();
                }
            }

        } catch (IOException e){
            System.out.println("Receiver: "+e.getMessage());
        }

    }
}
