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

    SessionInfo info;

    public ServerJob(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        info = new SessionInfo();
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            startReceiving();

            in.close();
            out.close();

        } catch (Exception e) {
            info.isFinished = true;
            throw new RuntimeException(e);
        }
    }

    void startReceiving() throws IOException {
        Thread speed = null;
        try {
            long size = in.readLong();
            try {
                info.fileName = in.readUTF();
            } catch (UTFDataFormatException e) {
                System.out.println("Wrong file name encoding!");
            }
            System.out.println("File to receive: " + info.fileName + ", size: " + size + " bytes\n");

            out.writeInt(SUCCESS);

            info.timeStart = System.currentTimeMillis();
            speed = new Thread(new TransferSpeed(info));
            speed.start();

            int bytes;
            File file = createFile("./uploads/" + info.fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            byte[] buffer = new byte[BUFFER_SIZE];
            while (size > 0 && (bytes = in.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                info.bytesReceived += bytes;
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes;
            }

            info.isFinished = true;
            Thread.sleep(500);

            if (size != 0) {
                System.out.printf("File \"%s\" corrupted! Please, try again.%n", info.fileName);
                out.writeInt(FAILURE);
            } else System.out.printf("File \"%s\" received, avg. speed:  %.3f MB/sec.%n",
                    info.fileName, info.avgSpeed);

            out.writeInt(SUCCESS);
            fileOutputStream.close();
            speed.join();
        } catch (Exception e){
            out.writeInt(FAILURE);
            info.isFinished = true;
            assert speed != null;
            speed.interrupt();
            this.socket.close();
        }
    }

    private File createFile(String name){
        int n = 0;
        File file = new File(name);
        if (file.exists()){
            while (true) {
                boolean res = file.renameTo(new File("./uploads/" + "(" + n + ")" + info.fileName));
                if (res) break;
                ++n;
            }
        }
        return file;
    }
}
