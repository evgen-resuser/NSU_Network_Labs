package com.evgen.tcp_transfer.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientMain {

    private String path;
    private InetAddress addr;
    private int port;

    private static final int SUCCESS = 1000;

    private DataOutputStream out;
    private DataInputStream in;

    public ClientMain(String path, InetAddress addr, int port) {
        this.path = path;
        this.addr = addr;
        this.port = port;
    }

    public void start(){
        try (Socket socket = new Socket(addr, port)){
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            sendFile();

            in.close();
            out.close();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void sendFile() throws IOException {
        File file = new File(path);
        long size = file.length();
        String name = file.getName();

        out.writeLong(size);
        out.writeUTF(name);

        int status = in.readInt();
        if (status != SUCCESS) {
            System.out.println("Error while sending!");
            System.exit(-1);
        }
        System.out.printf("The server got file info. Start sending file \"%s\"...\n", name);

        FileInputStream fileInputStream = new FileInputStream(file);

        int bytes = 0;
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            // Send the file to Server Socket
            out.write(buffer, 0, bytes);
            out.flush();
        }
        fileInputStream.close();

        status = in.readInt();
        if (status != SUCCESS) {
            System.out.println("Error while sending!");
        } else System.out.println("File sent successfully!");
    }
}
