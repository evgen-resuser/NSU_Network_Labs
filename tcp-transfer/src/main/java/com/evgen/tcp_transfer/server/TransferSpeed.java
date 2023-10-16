package com.evgen.tcp_transfer.server;

public class TransferSpeed implements Runnable{

    private final int MB_SIZE = 1024*1024;

    private long receivedBytes = 0;
    private final ServerJob job;

    public TransferSpeed(ServerJob job) {
        this.job = job;
    }

    @Override
    public void run() {
        long time1 = System.currentTimeMillis();
        int timePassed = 0;
        String fileName = job.fileName;

        double speedsSum = 0;
        double speedCount = 0;

        while(!job.isFinished){
            if (System.currentTimeMillis() - time1 >= 3000){
                timePassed += 3;
                time1 = System.currentTimeMillis();
                receivedBytes = job.bytess;

                speedCount++;
                double speed = (double) receivedBytes /timePassed/MB_SIZE;
                speedsSum += speed;
                System.out.printf("%s: current speed is %f MB/sec, avg. speed: %f MB/sec. %n",
                        fileName, speed, (speedsSum/speedCount));
            }
        }
    }
}
