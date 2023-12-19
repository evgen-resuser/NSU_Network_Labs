package org.evgen.network;

import me.ippolitov.fit.snakes.SnakesProto;
import org.evgen.controller.interfaces.Controller;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class NetworkMain implements MessageSender{

    private DatagramSocket socket;

    private Thread announcer;
    private final Thread multicastListener = new Thread(new MulticastService(this));
    private final Thread socketListener;

    private final Controller controller;

    public NetworkMain(Controller controller) {
        multicastListener.start();
        this.controller = controller;

        try {
            socket = new DatagramSocket(0);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        this.socketListener = new Thread(new SocketService(socket, controller));
        socketListener.start();
    }

    public void readMessage(DatagramPacket packet) {
        controller.deliverMessage(packet);
    }

    public void sendMessage(SnakesProto.GameMessage msg, InetAddress address, int port) {
        byte[] data = msg.toByteArray();
        try {
            socket.send(new DatagramPacket(data, 0, data.length, address, port));
            System.out.println("sent " + msg.getTypeCase() + " to " + address + ":" + port);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private GameAnnounce announce;

    public void startAnnounce(SnakesProto.GameMessage announcement) {
        announce = new GameAnnounce(announcement, socket);
        //announcer = new Thread(new GameAnnounce(announcement, socket));
        announcer = new Thread(announce);
        announcer.start();
    }

    public void stopAnnounce() {
        if (announcer == null || announcer.isInterrupted()) return;
        announcer.interrupt();
    }

    public void sendToAll(SnakesProto.GameMessage message) {

    }

}
