package org.evgen.controller;

import java.io.PrintStream;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Setter;
import me.ippolitov.fit.snakes.SnakesProto.GameMessage;
import me.ippolitov.fit.snakes.SnakesProto.GameMessage.PingMsg;
import org.evgen.controller.interfaces.Controller;
import org.evgen.network.MessageSender;
import org.evgen.protocol.PlayerInfo;

public class MessageRecvTimer implements Runnable {

    private Controller controller;
    private MessageSender sender;
    private GameContext context;

    private InetAddress masterAddress;
    private int masterPort;

    @Setter
    private int masterId;

    ConcurrentHashMap<Integer, Long> timesMap = new ConcurrentHashMap();
    ConcurrentHashMap<InetInfo, Integer> ips = new ConcurrentHashMap();

    @Setter
    private int curId;

    public MessageRecvTimer(Controller controller, MessageSender sender, GameContext context) {
        this.controller = controller;
        this.sender = sender;
        this.context = context;
    }

    public void setMasterInfo(InetAddress a, int port) {
        this.masterAddress = a;
        this.masterPort = port;
    }

    public synchronized void saveIp(InetAddress a, int port, int id) {
        this.ips.put(new InetInfo(a, port), id);
    }

    public synchronized void timeStamp(int id) {
        this.timesMap.put(id, System.currentTimeMillis());
    }

    public synchronized void timeStampIp(InetInfo info) {
        //System.out.println("find ip.....");
        Integer id = this.ips.get(info);
        if (id != null) {
            this.timesMap.put(id, System.currentTimeMillis());
            //System.out.println("added from ip");
        }
    }

    @Override
    public void run() {
        while(Thread.currentThread().isAlive()) {
            try {

                long curTime = System.currentTimeMillis();

                //System.out.println(context.getPlayersMap());
                for(Map.Entry<Integer, PlayerInfo> entry : context.getPlayersMap().entrySet()) {
                    if (entry.getKey() == masterId) {
                        //System.out.println("try to ping master...");
                        sender.sendMessage(
                                GameMessage.newBuilder().setPing(PingMsg.newBuilder().build()).setSenderId(curId).setMsgSeq(System.currentTimeMillis()).build(),
                                masterAddress, masterPort
                        );
                    } else if (entry.getKey() != curId) {
                        sender.sendMessage(
                                GameMessage.newBuilder().setPing(PingMsg.newBuilder().build()).setSenderId(curId).setMsgSeq(System.currentTimeMillis()).build(),
                                entry.getValue().getInetAddress(), entry.getValue().getPort()
                        );
                    }
                }

                for (Map.Entry<Integer, Long> e : timesMap.entrySet()) {
                    if (curTime - e.getValue() > 0.8 * context.getDelay()){
                        System.out.println("player " + e.getKey() + " is dead? diff: " + (curTime - e.getValue()));
                        controller.proceedDeath(e.getKey());
                        timesMap.remove(e.getKey());
                    }
                }


                Thread.sleep((long) (context.getDelay() * 0.2));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
