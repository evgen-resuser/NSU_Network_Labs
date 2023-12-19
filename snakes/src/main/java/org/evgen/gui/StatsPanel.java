package org.evgen.gui;

import me.ippolitov.fit.snakes.SnakesProto;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class StatsPanel extends JPanel {

    JPanel scoreboardPanel;

    public StatsPanel() {
        this.scoreboardPanel = new JPanel();
        scoreboardPanel.setPreferredSize(new Dimension(150, 100));
        scoreboardPanel.setBorder(new LineBorder(Color.GRAY));
        scoreboardPanel.setFocusable(false);

        initScoreboard();

        this.add(new JLabel("Scoreboard"));
        this.add(scoreboardPanel);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setFocusable(false);
    }

    private JList<String> playersList;
    private void initScoreboard() {
        playersList = new JList<>();
        playersList.setFocusable(false);
        playersList.setFixedCellWidth(100);

        scoreboardPanel.add(playersList);
    }

    public void applyPlayersList(List<SnakesProto.GamePlayer> playerList) {
        String[] players = new String[playerList.size()];
        int i = 0;
        for (SnakesProto.GamePlayer player : playerList) {
            String roleIcon = "";
            switch (player.getRole()) {
                case MASTER -> roleIcon = "★";
                case DEPUTY -> roleIcon = "☆";
                case NORMAL -> roleIcon = "○";
                case VIEWER -> {/*do nothing*/}
            }
            String name = String.format(
                    "%s %s %d", roleIcon, player.getName(), player.getScore()
            );
            players[i] = name;
            i++;
        }
        playersList.setListData(players);
    }

    public void updateStats() {
        //todo
    }
}
