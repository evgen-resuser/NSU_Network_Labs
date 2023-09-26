package com.evgen.clonesfinder.parser;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConsoleOptionsParser {
    private final AppArguments opts;

    public ConsoleOptionsParser(AppArguments opt){
        this.opts = opt;
    }

    public int getPort(){
        return opts.port;
    }

    public InetAddress getAddress() throws UnknownHostException {
        return InetAddress.getByName(opts.address);
    }
}
