package com.evgen.clonesfinder.multicast;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastCore {

    int port;
    InetAddress addr;
    char mode;

    public MulticastCore(int port, InetAddress addr, char mode){
        this.port = port;
        this.addr = addr;
        this.mode = mode;
    }

    public void startWork(){

        switch (mode){
            case 's':{
                System.out.println("Enabled Sending mode...");

                MulticastSender senderMC = new MulticastSender(port, addr);
                Thread sender = new Thread(senderMC);
                sender.start();

                break;
            }
            case 'r':{
                System.out.println("Enabled Receiving mode...");

                MulticastHandler handlerMC = new MulticastHandler();
                MulticastReceiver receiverMc = new MulticastReceiver(handlerMC, addr, port);

                Thread handler = new Thread(handlerMC);
                Thread receiver = new Thread(receiverMc);

                handler.start();
                receiver.start();

                break;
            }
        }

    }

}
