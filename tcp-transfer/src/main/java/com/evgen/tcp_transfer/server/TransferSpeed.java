package com.evgen.tcp_transfer.server;

public class TransferSpeed implements Runnable{

    private static final int MB_SIZE = 1024*1024;
    private static final int MILLIS = 1000;
    private static final int TIMEOUT = 3000;

    private final SessionInfo info;

    public TransferSpeed(SessionInfo info) {
        this.info = info;
    }

    @Override
    public void run() {
        long time1 = info.timeStart;
        long timePassed = 0;

        double speedsSum = 0;
        int speedCount = 0;

        double avg = 0;
        double speed;

        while(!info.isFinished){
            timePassed = System.currentTimeMillis() - info.timeStart;

            if (System.currentTimeMillis() - time1 >= TIMEOUT){
                speed = ((info.bytesReceived / (double)MB_SIZE) / (timePassed / (double)MILLIS));
                speedsSum += speed;
                speedCount ++;
                time1 = System.currentTimeMillis();
                avg = (speedsSum / speedCount);
                System.out.printf("%s: current speed is %.3f MB/sec, avg. speed is %.3f Mb/sec. %n",
                        info.fileName, speed, avg);
            }
        }
        if (timePassed < 3000){
            info.avgSpeed = (info.bytesReceived / (double) MB_SIZE) / (timePassed / (double) MILLIS);
        } else info.avgSpeed = avg;

    }
}
