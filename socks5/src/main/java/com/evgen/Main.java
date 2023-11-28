package com.evgen;

import com.evgen.proxy.ProxyServer;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No arguments");
            return;
        }
        int port = Integer.parseInt(args[0]);
        System.out.println(port);
        try(ProxyServer server = new ProxyServer(port)) {
            server.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

}