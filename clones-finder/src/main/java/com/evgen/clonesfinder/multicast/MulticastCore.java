package com.evgen.clonesfinder.multicast;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastCore {

    int port;
    InetAddress addr;
    public MulticastCore(int port, InetAddress addr){
        this.port = port;
        this.addr = addr;
    }

    public void startWork(){
        MulticastSender senderMC = new MulticastSender(port, addr);
        MulticastHandler handlerMC = new MulticastHandler();
        MulticastReceiver receiverMc = new MulticastReceiver(handlerMC, addr, port);

        System.out.println("Listening...");

        Thread sender = new Thread(senderMC);
        Thread handler = new Thread(handlerMC);
        Thread receiver = new Thread(receiverMc);

        sender.start();
        handler.start();
        receiver.start();
    }

}
