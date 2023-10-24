package com.evgen.tcp_transfer.server;

public class SessionInfo {

    String fileName;
    long bytesReceived;
    long fileSize;
    long timeStart;
    double avgSpeed;
    boolean isFinished = false;

}
