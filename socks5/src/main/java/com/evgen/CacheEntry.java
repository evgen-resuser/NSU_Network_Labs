package com.evgen;

public class CacheEntry {
    public int port;
    public byte[] addr;

    public CacheEntry(int port, byte[] addr) {
        this.port = port;
        this.addr = addr;
    }
}
