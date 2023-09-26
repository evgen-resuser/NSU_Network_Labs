package com.evgen.clonesfinder.multicast;

import com.evgen.clonesfinder.misc.ClientData;

import java.text.SimpleDateFormat;
import java.util.*;

public class MulticastHandler implements Runnable{

    private static final int TIME = 4000;

    private final Set<ClientData> clients = new HashSet<>();
    private final HashMap<ClientData, Long> timestamps = new HashMap<>();

    void printClients(){
        System.out.println("\n=====o=====\nAlive clients - " + new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())));
        int i = 1;
        for (ClientData client : clients){
            System.out.println(i + ") " +client);
            i++;
        }
        System.out.println("=====o=====");
    }

    void checkClients(){
        Set<ClientData> clientsCopy = new HashSet<>(clients);

        for (ClientData client : clientsCopy){
            if (System.currentTimeMillis() - timestamps.get(client) > TIME){
                timestamps.remove(client);
                clients.remove(client);
            }
        }
    }

    void addClient(ClientData client){

        clients.add(client);
        timestamps.put(client, System.currentTimeMillis() + TIME);
        //printClients();
    }

    @Override
    public void run() {
        while (true){
            checkClients();
            try {
                Thread.sleep(TIME);
            } catch (InterruptedException e){
                System.out.println(e.getLocalizedMessage());
            }
            printClients();
        }
    }
}
