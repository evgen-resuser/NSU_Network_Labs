package org.evgen.controller;

import lombok.Setter;
import me.ippolitov.fit.snakes.SnakesProto;
import org.evgen.controller.interfaces.Controller;
import org.evgen.controller.interfaces.StateApplicator;
import org.evgen.gui.BoardFrame;
import org.evgen.gui.interfaces.BoardView;
import org.evgen.gui.interfaces.ControlsView;
import org.evgen.network.NetworkMain;
import org.evgen.protocol.GameInfo;
import org.evgen.protocol.MessageBuilder;
import org.evgen.protocol.PlayerInfo;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;

public class MainController implements Controller, StateApplicator {

    private final GameContext context;
    private final NetworkMain networkMain;
    private final MessageBuilder messageBuilder;
    private GameCore gameCore;
    private TAdapter adapter;

    @Setter
    private ControlsView controls;
    private BoardView board;

    private int id;

    private InetAddress masterAddress;
    private int masterPort;
    private int masterId;

    private final HashSet<GameInfo> games = new HashSet<>();

    private SnakesProto.NodeRole currentRole;

    private long lastAnnMsgId = 0;

    public MainController(GameContext context) {
        this.context = context;
        this.networkMain = new NetworkMain(this);
        this.messageBuilder = new MessageBuilder(this.context);
        this.adapter = new TAdapter(this);

        id = (int)(System.currentTimeMillis() % 1000000);
        System.out.println("current client id: " + id);
    }

    ////////////////////// BASE METHODS //////////////////////

    public void stopGame() {
//        if (currentRole == SnakesProto.NodeRole.MASTER) {
//            networkMain.stopAnnounce();
//        }
//        currentRole = SnakesProto.NodeRole.VIEWER;
//        //todo delegate master work
//        gameCore.stopCore();
//        isGameOn = false;
        if (currentRole != SnakesProto.NodeRole.MASTER) {
            currentRole = SnakesProto.NodeRole.VIEWER;
            networkMain.sendMessage(messageBuilder.buildRoleChange(
                    SnakesProto.NodeRole.VIEWER, id, SnakesProto.NodeRole.MASTER, masterId
            ), masterAddress, masterPort);
            return;
        }
        networkMain.stopAnnounce();

        PlayerInfo dep = context.findRole(SnakesProto.NodeRole.DEPUTY);
        System.out.println("found dep:" + dep);
        if (dep != null) {
            System.out.println("tell about it to deputy!");
            networkMain.sendMessage(
                    messageBuilder.buildRoleChange(SnakesProto.NodeRole.VIEWER, id, SnakesProto.NodeRole.MASTER, dep.getId()),
                    dep.getInetAddress(), dep.getPort()
            );
        }

        gameCore.stopCore();
        //msgChecker.interrupt();

        isGameOn = false;

    }

    @Override
    public void deliverMessage(DatagramPacket packet) {
        int len = packet.getLength();

        byte[] data = Arrays.copyOfRange(packet.getData(), 0, len);

        try {
            SnakesProto.GameMessage message = SnakesProto.GameMessage.parseFrom(data);
            if (message.getMsgSeq() == lastAnnMsgId) return;

            switch (message.getTypeCase()) {
                case ANNOUNCEMENT -> {
                    GameInfo newGame =
                            new GameInfo(packet.getAddress(), packet.getPort(), message.getAnnouncement().getGames(0));
                    games.add(newGame);
                    controls.updateGames(games);
                }
                case JOIN -> {
                    System.out.println(message.getJoin().getPlayerName() +
                            " trying to join as "+message.getJoin().getRequestedRole());
                    if (!gameCore.canJoin(message.getJoin()))
                        networkMain.sendMessage( messageBuilder.buildError("the request was refused"),
                            packet.getAddress(), packet.getPort()
                    );
                    int newId = (int)(System.currentTimeMillis() % 1000000);

                    SnakesProto.NodeRole role = rolePicker(message.getJoin().getRequestedRole());
                    if (role == SnakesProto.NodeRole.DEPUTY) {
                        networkMain.sendMessage(messageBuilder.buildRoleChange(
                                SnakesProto.NodeRole.MASTER, id, role, newId
                        ), packet.getAddress(), packet.getPort());
                    }

                    PlayerInfo playerInfo = new PlayerInfo(
                            message.getJoin().getPlayerName(), newId, packet.getAddress().toString(),
                            packet.getPort(), role, message.getJoin().getPlayerType(),
                            0
                    );

                    //gameCore.addPlayer(message.getJoin(), newId);
                    gameCore.addPlayer(playerInfo);
                    networkMain.sendMessage(
                            messageBuilder.buildAck(id, newId, message.getMsgSeq()), packet.getAddress(), packet.getPort()
                    );

                }
                case ERROR -> {
                    System.out.println("got ERR: "+message.getError().getErrorMessage());
                    //currentRole = SnakesProto.NodeRole.VIEWER;
                    if (message.getError().getErrorMessage().equals("[test] master death")) becomeMaster();
                }
                case ACK -> {
                    System.out.printf("ACK! r: %d, s: %d%n", message.getReceiverId(), message.getSenderId());
                    id = message.getReceiverId();
                    System.out.println("id now: " + id);
                    masterId = message.getSenderId();
                }
                case STATE -> {
                    board.applyState(message.getState().getState());
                    lastStateMessage = message;
                }
                case PING -> networkMain.sendMessage(
                        SnakesProto.GameMessage.newBuilder().setMsgSeq(System.currentTimeMillis()).setPing(
                                SnakesProto.GameMessage.PingMsg.newBuilder().build()
                        ).build(), packet.getAddress(), packet.getPort()
                );
                case STEER -> {
                    System.out.println("got other steer");
                    deliverSteer(message);
                    networkMain.sendMessage(
                            messageBuilder.buildAck(id, message.getSenderId(), message.getMsgSeq()), packet.getAddress(), packet.getPort()
                    );
                }
                case ROLE_CHANGE -> {
//                    System.out.println(message.getRoleChange().getSenderRole() + " decided to make us "+message.getRoleChange().getReceiverRole());
//                    currentRole = message.getRoleChange().getReceiverRole();
//                    if (message.getRoleChange().getSenderRole() == SnakesProto.NodeRole.MASTER) {
//                        masterAddress = packet.getAddress();
//                        masterPort = packet.getPort();
//                    }
                    roleProceed(message, packet.getAddress(), packet.getPort());

                    networkMain.sendMessage(
                            messageBuilder.buildAck(id, message.getSenderId(), message.getMsgSeq()), packet.getAddress(), packet.getPort()
                    );
                }
                case DISCOVER -> networkMain.sendMessage(messageBuilder.buildAnnounce(), packet.getAddress(), packet.getPort());
                case TYPE_NOT_SET -> System.out.println("Message type was not set!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void roleProceed(SnakesProto.GameMessage message, InetAddress a, int port) {
        SnakesProto.GameMessage.RoleChangeMsg m = message.getRoleChange();
        switch (m.getSenderRole()) {
            case NORMAL -> {/*do nothing*/}
            case MASTER -> {
                masterAddress = a;
                masterPort = port;
                masterId = message.getSenderId();
                if (m.getSenderRole() == SnakesProto.NodeRole.VIEWER) {
                    System.out.println("you ded");
                    context.changeRole(message.getReceiverId(), SnakesProto.NodeRole.VIEWER);
                }
                if (m.getReceiverRole() == SnakesProto.NodeRole.DEPUTY) {
                    currentRole = SnakesProto.NodeRole.DEPUTY;
                    System.out.println("now im deputy!!!");
                }
            }
            case DEPUTY -> {/*do nothing1*/}
            case VIEWER -> {
                if (m.getReceiverRole() == SnakesProto.NodeRole.DEPUTY) {
                    currentRole = SnakesProto.NodeRole.DEPUTY;
                    System.out.println("now im deputy!!!");
                } else if (m.getReceiverRole() == SnakesProto.NodeRole.MASTER) {
                    if (currentRole != SnakesProto.NodeRole.MASTER) {
                        System.out.println("im gonna be master!!!");
                        becomeMaster();
                    }
                }
                context.changeRole(message.getSenderId(), SnakesProto.NodeRole.VIEWER);
                System.out.println("!!!!!!!!!!" + context.getPlayersMap().get(message.getSenderId()).getRole());
            }
        }
    }

    @Override
    public void getState(SnakesProto.GameState state) {
        SnakesProto.GameMessage stateMsg = messageBuilder.buildStateMsg(state);
        //System.out.println(stateMsg);

        if (currentRole != SnakesProto.NodeRole.MASTER) return;
        //System.out.println("ready to send from controller!!!!!");
        for (PlayerInfo player : context.getPlayersMap().values()) {
        //for (PlayerInfo player : context.getPlayers()) {
            if (player.hasIpInfo()) networkMain.sendMessage(stateMsg,
                    player.getInetAddress(), player.getPort());
        }
        //networkMain.updateAnnounce(messageBuilder.buildAnnounce());
        board.applyState(state);
    }

    ////////////////////// MASTER METHODS //////////////////////

    private boolean isGameOn;

    public void startGame() {
        if (isGameOn) return;
        isGameOn = true;
        currentRole = SnakesProto.NodeRole.MASTER;
        PlayerInfo m = new PlayerInfo(
                context.getPlayerName(), id, SnakesProto.NodeRole.MASTER,
                SnakesProto.PlayerType.HUMAN, 0);

        //context.addMaster(m);
        gameCore = new GameCore(context, this);

        //context.addNewPlayer(m);
        gameCore.addPlayer(m);
        gameCore.startCore();

        SnakesProto.GameMessage ann = messageBuilder.buildAnnounce();
        lastAnnMsgId = ann.getMsgSeq();
        networkMain.startAnnounce(ann);


        board = new BoardFrame(ann.getAnnouncement().getGames(0).getConfig(), adapter);
    }

    private void deliverSteer(SnakesProto.GameMessage m) {
        gameCore.applySteer(m);
    }

    public SnakesProto.NodeRole rolePicker(SnakesProto.NodeRole requestedRole) {
        if (requestedRole == SnakesProto.NodeRole.VIEWER) return SnakesProto.NodeRole.VIEWER;

        if (requestedRole == SnakesProto.NodeRole.NORMAL && context.findRole(SnakesProto.NodeRole.DEPUTY) == null) {
            return SnakesProto.NodeRole.DEPUTY;
        }

        return SnakesProto.NodeRole.NORMAL;
    }

    ////////////////////// PLAYER METHODS //////////////////////

    public void connect(GameInfo info, boolean isViewer) {
        if (info == null) return;

        System.out.println("try to connect...");
        networkMain.sendMessage(messageBuilder.buildJoinMsg(info, isViewer), info.getAddr(), info.getPort());

        board = new BoardFrame(info.getGameSpecs().getConfig(), adapter);
        currentRole = isViewer ? SnakesProto.NodeRole.VIEWER : SnakesProto.NodeRole.NORMAL;

        masterAddress = info.getAddr();
        masterPort = info.getPort();

        context.getFromGameInfo(info);
    }

    public void applySteerMsg(SnakesProto.Direction d) {
        //System.out.println(id + " wants to steer");

        SnakesProto.GameMessage m = SnakesProto.GameMessage.newBuilder()
                .setMsgSeq(System.currentTimeMillis())
                .setSteer(SnakesProto.GameMessage.SteerMsg.newBuilder()
                        .setDirection(d)
                        .build())
                .setSenderId(id)
                .build();

        if (currentRole == SnakesProto.NodeRole.MASTER) {
            deliverSteer(m);
            return;
        }
        networkMain.sendMessage(m, masterAddress, masterPort);
    }

    private SnakesProto.GameMessage lastStateMessage;

    public void becomeMaster() {
        currentRole = SnakesProto.NodeRole.MASTER;

        SnakesProto.GameState state = lastStateMessage.getState().getState();

        int stateOrder = state.getStateOrder();


        context.getFromGameState(state);


        gameCore = new GameCore(context, this);


        SnakesProto.GameMessage ann = messageBuilder.buildAnnounce();

        lastAnnMsgId = ann.getMsgSeq();
        networkMain.startAnnounce(ann);

        gameCore.getListsFromState(state);

        gameCore.setStateCounter(stateOrder);

        gameCore.startCore();

        PlayerInfo lastMaster = context.getPlayersMap().get(masterId);
        lastMaster.setIp(masterAddress.toString());
        lastMaster.setPort(masterPort);
        context.addNewPlayer(lastMaster);

        for (PlayerInfo player : context.getPlayersMap().values()) {
            if (player.getRole() == SnakesProto.NodeRole.VIEWER) continue;
            if (player.getId() == id) continue;
            networkMain.sendMessage(messageBuilder.buildRoleChange(
                    SnakesProto.NodeRole.MASTER, id, SnakesProto.NodeRole.NORMAL, player.getId()
            ), player.getInetAddress(), player.getPort());
        }
    }

    public void switchMaster() {
        SnakesProto.GamePlayers players = lastStateMessage.getState().getState().getPlayers();

        for (SnakesProto.GamePlayer player : players.getPlayersList()) {
            if (player.getRole() == SnakesProto.NodeRole.DEPUTY) {
                try {
                    masterAddress = InetAddress.getByName(player.getIpAddress().substring(1));
                } catch (UnknownHostException e) {
                    System.out.println(e.getMessage());
                }
                masterPort = player.getPort();
                System.out.println("deputy is now our master! "+masterAddress+":"+masterPort);
                break;
            }
        }
        System.out.println("There is no deputy!");
    }

    public void testDeath() {
        stopGame();
        InetAddress a = context.findRole(SnakesProto.NodeRole.DEPUTY).getInetAddress();
        int port = context.findRole(SnakesProto.NodeRole.DEPUTY).getPort();

        networkMain.sendMessage(messageBuilder.buildError("master death"), a, port);
        System.exit(1);
    }

}
