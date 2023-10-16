package com.evgen.tcp_transfer.server;

import java.io.*;
import java.net.Socket;

public class ServerJob implements Runnable{

    private final Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private static final int SUCCESS = 1000;
    private static final int FAILURE = 2000;

    private static final int BUFFER_SIZE = 4*1024;

    boolean isFinished = false;

    public ServerJob(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            startReceiving();

            in.close();
            out.close();

        } catch (Exception e) {
            isFinished = true;
            throw new RuntimeException(e);
        }
    }

    String fileName;
    long bytess = 0;

    void startReceiving() throws IOException, InterruptedException {

        long size = in.readLong();
        fileName = in.readUTF();
        System.out.println("File to receive: "+fileName+", size: "+size+" bytes\n");

        out.writeInt(SUCCESS);

        Thread speed = new Thread(new TransferSpeed(this));
        speed.start();

        int bytes = 0;
        File file = createFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        byte[] buffer = new byte[BUFFER_SIZE];
            while (size > 0 && (bytes = in.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes; // read upto file size
                bytess += bytes;
            }

        isFinished = true;
        if (size != 0) {
            System.out.printf("File \"%s\" corrupted! Please, try again.%n", fileName);
            out.writeInt(FAILURE);
        } else System.out.printf("File \"%s\" Received%n", fileName);

        out.writeInt(SUCCESS);
        fileOutputStream.close();
        speed.join();
    }

    private File createFile(){
        int n = 0;
        File file = new File("./uploads/"+fileName);
        if (file.exists()){
            while (true) {
                boolean res = file.renameTo(new File("./uploads/" + "(" + n + ")" + fileName));
                if (res) break;
                ++n;
            }
        }
        return file;
    }
}
