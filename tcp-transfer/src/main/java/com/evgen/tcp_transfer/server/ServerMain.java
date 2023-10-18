package com.evgen.tcp_transfer.server;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    private static final int THREADS_COUNT = 10;

    private int port;

    public ServerMain(int port) {
        this.port = port;

        new File("./uploads").mkdirs();
    }

    public void start(){

        try (ServerSocket serverSocket = new ServerSocket(port)){

            System.out.println("Server started!");

            ExecutorService clientThreadPool = Executors.newFixedThreadPool(THREADS_COUNT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New connection: "+socket.getInetAddress()+":"+socket.getPort());
                clientThreadPool.execute(new ServerJob(socket));
            }

        } catch (RuntimeException e){
            System.out.println("Connection reset!");
        } catch (Exception e ){
            System.out.println(e.getMessage());
        }

    }
}
