package com.evgen.clonesfinder;

import com.evgen.clonesfinder.multicast.MulticastCore;
import com.evgen.clonesfinder.parser.AppArguments;
import com.evgen.clonesfinder.parser.ConsoleOptionsParser;
import com.google.devtools.common.options.OptionsParser;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class App
{
    public static void main( String[] args ) {

        OptionsParser parser = OptionsParser.newOptionsParser(AppArguments.class);
        parser.parseAndExitUponError(args);

        ConsoleOptionsParser options = new ConsoleOptionsParser(parser.getOptions(AppArguments.class));

        int port = options.getPort();
        if (port <= 0){
            System.out.println("Wrong port! Type \"-h\" for help");
            return;
        }

        InetAddress addr = null;
        try {
            addr = options.getAddress();
        } catch (UnknownHostException e){
            System.out.println(e.getLocalizedMessage());
            return;
        }
        if (!addr.isMulticastAddress()){
            System.out.println(addr + " is not Multicast address!\n");
            return;
        }

        new MulticastCore(port, addr).startWork();
    }
}
