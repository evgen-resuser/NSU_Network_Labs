package com.evgen.tcp_transfer.params_parser;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConsoleOptionsParser {
    private final ConsoleArguments opts;

    public ConsoleOptionsParser(ConsoleArguments opt){
        this.opts = opt;
    }

    public int getPort(){
        return opts.port;
    }

    public InetAddress getAddress() throws UnknownHostException {
        return InetAddress.getByName(opts.addr);
    }

    public boolean getHelp(){
        return opts.help;
    }

    public boolean isServer(){
        return opts.serverMode;
    }

    public boolean isClient(){
        return opts.clientMode;
    }

    public String getPath(){
        return opts.filePath;
    }

}
