package com.evgen.clonesfinder.misc;

import java.net.InetAddress;
import java.util.Objects;

public class ClientData {
    private InetAddress addr;
    private int port;

    public ClientData(InetAddress addr, int port) {
        this.addr = addr;
        this.port = port;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ClientData other = (ClientData)obj;
        String otherInfo = other.toString();
        return otherInfo.equals(this.toString());
    }

    @Override
    public String toString(){
        return "Client: "+this.addr+":"+this.port;
    }

    @Override
    public int hashCode() {
        return Objects.hash(addr, port);
    }

}
