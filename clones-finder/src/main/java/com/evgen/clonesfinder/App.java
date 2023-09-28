package com.evgen.clonesfinder;

import com.evgen.clonesfinder.multicast.MulticastCore;
import com.evgen.clonesfinder.parser.AppArguments;
import com.evgen.clonesfinder.parser.ConsoleOptionsParser;
import com.google.devtools.common.options.OptionsParser;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;

public class App
{
    public static void main( String[] args ) {

        OptionsParser parser = OptionsParser.newOptionsParser(AppArguments.class);
        parser.parseAndExitUponError(args);

        ConsoleOptionsParser options = new ConsoleOptionsParser(parser.getOptions(AppArguments.class));

        boolean help = options.getHelp();
        if (help){
            System.out.println("Help");
            System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(),
                    OptionsParser.HelpVerbosity.LONG));
            return;
        }

        int port = options.getPort();
        if (port <= 0){
            System.out.println("Wrong port! Rerun with \"-h\" for help");
            return;
        }

        InetAddress addr;
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

        char mode = (options.getMode()).charAt(0);
        if (mode != 's' && mode != 'r'){
            System.out.println("Wrong mode! Rerun with \"-h\" for help");
            return;
        }

        new MulticastCore(port, addr, mode).startWork();
    }
}
