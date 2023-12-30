package org.evgen.protocol;

import lombok.Getter;
import lombok.Setter;
import me.ippolitov.fit.snakes.SnakesProto;

import java.net.InetAddress;

@Getter
@Setter
public class PlayerInfo {
    private String name;
    private int id;
    private String ip;
    private int port;
    private SnakesProto.NodeRole role;
    private SnakesProto.PlayerType type;
    private int score;

    public PlayerInfo(String name, int id, String ip, int port, SnakesProto.NodeRole role, SnakesProto.PlayerType type, int score) {
        this.name = name;
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.role = role;
        this.type = type;
        this.score = score;
    }

    public PlayerInfo(String name, int id, SnakesProto.NodeRole role, SnakesProto.PlayerType type, int score) {
        this.name = name;
        this.id = id;
        this.role = role;
        this.type = type;
        this.score = score;
        this.ip = null;
        this.port = -1;
    }

    public void incScore() {
        score++;
    }

    public boolean hasIpInfo() {
        return (ip != null) && (port != -1);
    }

    public InetAddress getInetAddress() {
        try {
             return InetAddress.getByName(ip.substring(1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return id + ": " + name + " " + role;
    }
}
