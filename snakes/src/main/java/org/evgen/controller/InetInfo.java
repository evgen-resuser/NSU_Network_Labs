package org.evgen.controller;

import java.net.InetAddress;

public class InetInfo {
    private InetAddress address;
    private int port;

    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == InetInfo.class) {
            return this.address == ((InetInfo)obj).address && this.port == ((InetInfo)obj).port;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.address.hashCode() + this.port;
    }

    public InetInfo(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }
}

