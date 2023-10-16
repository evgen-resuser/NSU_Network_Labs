package com.evgen.tcp_transfer;

import com.evgen.tcp_transfer.client.ClientMain;
import com.evgen.tcp_transfer.params_parser.ConsoleArguments;
import com.evgen.tcp_transfer.params_parser.ConsoleOptionsParser;
import com.evgen.tcp_transfer.server.ServerMain;
import com.google.devtools.common.options.OptionsParser;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        OptionsParser parser = OptionsParser.newOptionsParser(ConsoleArguments.class);
        parser.parseAndExitUponError(args);

        ConsoleOptionsParser options = new ConsoleOptionsParser(parser.getOptions(ConsoleArguments.class));

        if (options.getHelp()){
            System.out.println("TCP file transfer help:");
            System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(),
                    OptionsParser.HelpVerbosity.LONG));
            return;
        }

        if (options.isClient() == options.isServer()){
            System.out.println("Choose only one working mode: -c (client) / -s (server)!\n");
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

        if (options.isClient()){
            new ClientMain(options.getPath(), addr, port).start();
        } else {
            new ServerMain(port).start();
        }

    }
}